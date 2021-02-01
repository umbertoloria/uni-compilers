package app.visitor.clanggenerator.exprvisitor;

import app.node.BinaryOperationNode;
import app.node.ExprNode;
import app.node.TypeNode;
import app.node.binop.*;
import app.node.expr.*;
import app.visitor.ExclusiveNodeVisitor;
import app.visitor.clanggenerator.ClangCodeEditor;
import app.visitor.clanggenerator.EasyNamesManager;

import java.util.LinkedList;
import java.util.List;

public class CExprGeneratorVisitor extends ExclusiveNodeVisitor<List<String>> {

	private ClangCodeEditor clangCodeEditor;
	private EasyNamesManager easyNamesManager;

	public CExprGeneratorVisitor(ClangCodeEditor clangCodeEditor, EasyNamesManager easyNamesManager) {
		this.clangCodeEditor = clangCodeEditor;
		this.easyNamesManager = easyNamesManager;
	}

	private List<String> getSingletonList(String str) {
		List<String> result = new LinkedList<>();
		result.add(str);
		return result;
	}

	@Override
	public List<String> visitId(Id id) {
		String usableName = easyNamesManager.safeNameUsage(id.name);
		return getSingletonList(usableName);
	}

	@Override
	public List<String> visitTrue(True aTrue) {
		return getSingletonList("1");
	}

	@Override
	public List<String> visitFalse(False aFalse) {
		return getSingletonList("0");
	}

	@Override
	public List<String> visitIntConst(IntConst intConst) {
		return getSingletonList(intConst.value + "");
	}

	@Override
	public List<String> visitFloatConst(FloatConst floatConst) {
		return getSingletonList(floatConst.value + "");
	}

	@Override
	public List<String> visitStringConst(StringConst stringConst) {
		return getSingletonList("\"" + stringConst.str + "\"");
	}

	@Override
	public List<String> visitNotOP(NotOP notOP) {
		// parentesi necessarie
		return getSingletonList("!(" + notOP.expr.accept(this).get(0) + ")");
	}

	@Override
	public List<String> visitUMinusOP(UMinusOP uMinusOP) {
		// parentesi necessarie
		return getSingletonList("-(" + uMinusOP.expr.accept(this).get(0) + ")");
	}

	@Override
	public List<String> visitCallProcOP(CallProcOP callProcOP) {
		// L'unica espressione Toy che non sempre restituisce una sola espressione C equivalente è l'invocazione di una
		// procedura. Quando un'espressione Toy descrive una invocazione di procedura che restituisce più di un valore,
		// bisogna necessariamente costruire del codice di servizio per realizzarla. Una espressione C non basta per
		// modellare la restituzione di tutti questi valori. Ne verrà costruita una per ogni valore restituito.
		String procName = easyNamesManager.safeNameUsage(callProcOP.procId.name);
		if (callProcOP.type.contains(",")) {
			List<String> cParamExprs = new LinkedList<>();
			if (callProcOP.exprs != null) {
				for (ExprNode exprNode : callProcOP.exprs) {
					List<String> cLocalExprs = exprNode.accept(this);
					cParamExprs.addAll(cLocalExprs);
				}
			}
			String structName = callProcOP.type.replace(",", "_");
			// Per migliorare la leggibilità del codice C generato, il nome della variabile di servizio che permette
			// la restituzione multipla di valori da un'invocazione è basato sulla procedura stessa che viene invocata.
			String newVarName = easyNamesManager.createName(procName);
			clangCodeEditor.invokeWithResult(structName, newVarName, procName, cParamExprs);
			int retExprsCount = callProcOP.type.split(",").length;
			List<String> cRetExprs = new LinkedList<>();
			for (int i = 0; i < retExprsCount; i++) {
				cRetExprs.add(newVarName + ".t" + i);
			}
			return cRetExprs;
		} else {
			StringBuilder code = new StringBuilder();
			code.append(procName).append("(");
			if (callProcOP.exprs != null) {
				for (ExprNode expr : callProcOP.exprs) {
					List<String> cExprs = expr.accept(this);
					for (String cExpr : cExprs) {
						code.append(cExpr);
						code.append(", ");
					}
				}
				code.delete(code.length() - 2, code.length());
			}
			code.append(")");
			return getSingletonList(code.toString());
		}
	}


	// COMPARISON OPERATIONS
	private List<String> visitComparisionOperation(BinaryOperationNode bon) {
		String cExpr1 = bon.a.accept(this).get(0);
		String cExpr2 = bon.b.accept(this).get(0);
		if (bon.a.type.equals(TypeNode.STRING.getStringType()) && bon.b.type.equals(TypeNode.STRING.getStringType())) {
			// confronto di stringhe
			return getSingletonList("strcmp(" + cExpr1 + ", " + cExpr2 + ") " + bon.op + " 0");
		} else {
			// confronto di numeri
			return getSingletonList(cExpr1 + " " + bon.op + " " + cExpr2);
		}
	}

	@Override
	public List<String> visitLTOP(LTOP ltop) {
		return visitComparisionOperation(ltop);
	}

	@Override
	public List<String> visitLEOP(LEOP leop) {
		return visitComparisionOperation(leop);
	}

	@Override
	public List<String> visitGTOP(GTOP gtop) {
		return visitComparisionOperation(gtop);
	}

	@Override
	public List<String> visitGEOP(GEOP geop) {
		return visitComparisionOperation(geop);
	}

	@Override
	public List<String> visitEQOP(EQOP eqop) {
		return visitComparisionOperation(eqop);
	}

	// BOOLEAN BINARY OPERATIONS
	private List<String> visitBooleanBinaryOperation(BinaryOperationNode bon) {
		return getSingletonList(bon.a.accept(this).get(0) + " " + bon.op + " " + bon.b.accept(this).get(0));
	}

	@Override
	public List<String> visitAndOP(AndOP andOP) {
		return visitBooleanBinaryOperation(andOP);
	}

	@Override
	public List<String> visitOrOP(OrOP orOP) {
		return visitBooleanBinaryOperation(orOP);
	}

	// BINARY ARITHMETIC OPERATIONS
	private List<String> visitBinaryArithmeticOperation(BinaryOperationNode bon) {
		return getSingletonList(bon.a.accept(this).get(0) + " " + bon.op + " " + bon.b.accept(this).get(0));
	}

	@Override
	public List<String> visitTimesOP(TimesOP timesOP) {
		return visitBinaryArithmeticOperation(timesOP);
	}

	@Override
	public List<String> visitDivOP(DivOP divOP) {
		return visitBinaryArithmeticOperation(divOP);
	}

	@Override
	public List<String> visitPlusOP(PlusOP plusOP) {
		return visitBinaryArithmeticOperation(plusOP);
	}

	@Override
	public List<String> visitMinusOP(MinusOP minusOP) {
		return visitBinaryArithmeticOperation(minusOP);
	}

}
