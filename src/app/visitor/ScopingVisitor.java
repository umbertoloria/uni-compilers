package app.visitor;

import app.node.*;
import app.node.binop.*;
import app.node.expr.*;
import app.node.stat.*;
import app.visitor.scope.ScopingTable;

public class ScopingVisitor implements INodeVisitor {

	private ScopingTable scopingTable;

	@Override
	public Object visitProgramOP(ProgramOP programOP) {
		// Nuovo scope
		scopingTable = new ScopingTable("global");
		if (programOP.getVarDecls() != null) {
			for (VarDeclOP varDecl : programOP.getVarDecls()) {
				varDecl.accept(this);
			}
		}
		for (ProcOP proc : programOP.getProcs()) {
			proc.accept(this);
		}
		scopingTable.resolveUnmatchedUsages();
		return scopingTable;
	}

	@Override
	public Object visitVarDeclOP(VarDeclOP varDeclOP) {
		// Dichiarazione variabili
		for (IdInitOP idInit : varDeclOP.getIdInits()) {
			scopingTable.declareVariable(idInit.getId().getName(), varDeclOP.getType().getStringType());
			if (idInit.getExpr() != null) {
				idInit.getExpr().accept(this);
			}
		}
		return null;
	}

	@Override
	public Object visitTypeNode(TypeNode typeNode) {
		return null;
	}

	@Override
	public Object visitIdInitOP(IdInitOP idInitOP) {
		return null;
	}

	@Override
	public Object visitProcOP(ProcOP procOP) {
		// Dichiarazione funzione
		StringBuilder procedureType = new StringBuilder();
		if (procOP.getParDecls() != null) {
			for (ParDeclOP parDecl : procOP.getParDecls())
				procedureType.append((parDecl.getType().getStringType() + ",").repeat(parDecl.getIds().size()));
			procedureType.deleteCharAt(procedureType.length() - 1);
		}
		procedureType.append("->");
		for (ResultTypeNode returnType : procOP.getReturnTypes()) {
			procedureType.append(returnType.getStringType());
			procedureType.append(',');
		}
		procedureType.deleteCharAt(procedureType.length() - 1);
		scopingTable.declareProcedure(procOP.getId().getName(), procedureType.toString());
		// Nuovo scope
		scopingTable = scopingTable.createChild(procOP.getId().getName());
		if (procOP.getParDecls() != null) {
			for (ParDeclOP parDecl : procOP.getParDecls()) {
				parDecl.accept(this);
			}
		}
		if (procOP.getProcBody() != null) {
			procOP.getProcBody().accept(this);
		}
		scopingTable = scopingTable.getParent();
		return null;
	}

	@Override
	public Object visitParDeclOP(ParDeclOP parDeclOP) {
		// Dichiarazione parametri
		for (Id id : parDeclOP.getIds()) {
			scopingTable.declareVariable(id.getName(), parDeclOP.getType().getStringType());
		}
		return null;
	}

	@Override
	public Object visitResultTypeNode(ResultTypeNode resultTypeNode) {
		return null;
	}

	@Override
	public Object visitProcBodyOP(ProcBodyOP procBodyOP) {
		// Dichiarazione variabili
		if (procBodyOP.getVarDecls() != null) {
			for (VarDeclOP varDecl : procBodyOP.getVarDecls()) {
				varDecl.accept(this);
			}
		}
		if (procBodyOP.getBody() != null) {
			procBodyOP.getBody().accept(this);
		}
		if (procBodyOP.getReturnExpressions() != null) {
			for (ExprNode returnExpression : procBodyOP.getReturnExpressions()) {
				returnExpression.accept(this);
			}
		}
		return null;
	}

	@Override
	public Object visitBodyOP(BodyOP bodyOP) {
		for (StatNode stmt : bodyOP.getStmts()) {
			stmt.accept(this);
		}
		return null;
	}

	@Override
	public Object visitIfOP(IfOP ifOP) {
		ifOP.getExpr().accept(this);
		ifOP.getIfBody().accept(this);
		if (ifOP.getElifs() != null) {
			for (ElifOP elif : ifOP.getElifs()) {
				elif.accept(this);
			}
		}
		if (ifOP.getElseBody() != null) {
			ifOP.getElseBody().accept(this);
		}
		return null;
	}

	@Override
	public Object visitElifOP(ElifOP elifOP) {
		elifOP.getExpr().accept(this);
		elifOP.getBody().accept(this);
		return null;
	}

	@Override
	public Object visitWhileOP(WhileOP whileOP) {
		if (whileOP.getPreStmts() != null) {
			whileOP.getPreStmts().accept(this);
		}
		whileOP.getExpr().accept(this);
		whileOP.getIterStmts().accept(this);
		return null;
	}

	@Override
	public Object visitAssignOP(AssignOP assignOP) {
		// TODO: type-matching
		for (Id id : assignOP.getIds()) {
			id.accept(this);
		}
		for (ExprNode expr : assignOP.getExprs()) {
			expr.accept(this);
		}
		return null;
	}

	@Override
	public Object visitReadlnOP(ReadlnOP readlnOP) {
		for (Id id : readlnOP.getIds()) {
			id.accept(this);
		}
		return null;
	}

	@Override
	public Object visitWriteOP(WriteOP writeOP) {
		for (ExprNode expr : writeOP.getExprs()) {
			expr.accept(this);
		}
		return null;
	}

	@Override
	public Object visitId(Id id) {
		scopingTable.useVariable(id.getName());
		return null;
	}

	@Override
	public Object visitNull(Null aNull) {
		return null;
	}

	@Override
	public Object visitTrue(True aTrue) {
		return null;
	}

	@Override
	public Object visitFalse(False aFalse) {
		return null;
	}

	@Override
	public Object visitIntConst(IntConst intConst) {
		return null;
	}

	@Override
	public Object visitFloatConst(FloatConst floatConst) {
		return null;
	}

	@Override
	public Object visitStringConst(StringConst stringConst) {
		return null;
	}

	@Override
	public Object visitNotOP(NotOP notOP) {
		// TODO: only booleans
		notOP.getExpr().accept(this);
		return null;
	}

	@Override
	public Object visitUMinusOP(UMinusOP uMinusOP) {
		// TODO: only for INT_CONST and FLOAT_CONST
		uMinusOP.getExpr().accept(this);
		return null;
	}

	@Override
	public Object visitCallProcOP(CallProcOP callProcOP) {
		scopingTable.useProcedure(callProcOP.getProcId().getName());
		if (callProcOP.getExprs() != null) {
			for (ExprNode expr : callProcOP.getExprs()) {
				expr.accept(this);
			}
		}
		return null;
	}

	@Override
	public Object visitLTOP(LTOP ltop) {
		// TODO: only numbers
		ltop.getA().accept(this);
		ltop.getB().accept(this);
		return null;
	}

	@Override
	public Object visitLEOP(LEOP leop) {
		// TODO: only numbers
		leop.getA().accept(this);
		leop.getB().accept(this);
		return null;
	}

	@Override
	public Object visitGTOP(GTOP gtop) {
		// TODO: only numbers
		gtop.getA().accept(this);
		gtop.getB().accept(this);
		return null;
	}

	@Override
	public Object visitGEOP(GEOP geop) {
		// TODO: only numbers
		geop.getA().accept(this);
		geop.getB().accept(this);
		return null;
	}

	@Override
	public Object visitEQOP(EQOP eqop) {
		// TODO: only numbers
		eqop.getA().accept(this);
		eqop.getB().accept(this);
		return null;
	}

	@Override
	public Object visitAndOP(AndOP andOP) {
		// TODO: only booleans
		andOP.getA().accept(this);
		andOP.getB().accept(this);
		return null;
	}

	@Override
	public Object visitOrOP(OrOP orOP) {
		// TODO: only booleans
		orOP.getA().accept(this);
		orOP.getB().accept(this);
		return null;
	}

	@Override
	public Object visitTimesOP(TimesOP timesOP) {
		// TODO: only numbers
		timesOP.getA().accept(this);
		timesOP.getB().accept(this);
		return null;
	}

	@Override
	public Object visitDivOP(DivOP divOP) {
		// TODO: only numbers
		divOP.getA().accept(this);
		divOP.getB().accept(this);
		return null;
	}

	@Override
	public Object visitPlusOP(PlusOP plusOP) {
		// TODO: only numbers
		plusOP.getA().accept(this);
		plusOP.getB().accept(this);
		return null;
	}

	@Override
	public Object visitMinusOP(MinusOP minusOP) {
		// TODO: only numbers
		minusOP.getA().accept(this);
		minusOP.getB().accept(this);
		return null;
	}

}
