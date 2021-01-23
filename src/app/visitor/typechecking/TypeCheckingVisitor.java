package app.visitor.typechecking;

import app.node.*;
import app.node.binop.*;
import app.node.expr.*;
import app.node.stat.*;
import app.visitor.DFSBaseVisitor;
import app.visitor.INodeVisitor;
import app.visitor.scoping.ScopingTable;

import java.util.stream.Collectors;

public class TypeCheckingVisitor extends DFSBaseVisitor implements INodeVisitor {

	private ScopingTable scopingTable;
	private TypeUtils typeUtils = new TypeUtils();

	public TypeCheckingVisitor(ScopingTable scopingTable) {
		this.scopingTable = scopingTable;
	}

	@Override
	public Object visitVarDeclOP(VarDeclOP varDeclOP) {
		super.visitVarDeclOP(varDeclOP);
		String varType = varDeclOP.type.getStringType();
		for (IdInitOP idInit : varDeclOP.idInits) {
			if (idInit.expr != null) {
				typeUtils.assertCanAssign(idInit.expr.type, varType);
			}
		}
		return null;
	}

	@Override
	public Object visitProcOP(ProcOP procOP) {
		scopingTable = scopingTable.getScopingTableOf(procOP.id.name);
		procOP.procBody.accept(this);
		scopingTable = scopingTable.getParent();
		// Type-matching formal and actual return expressions
		String procType = scopingTable.getTypeOfCloser(procOP.id.name);
		String formalRetExprType = procType.substring(procType.indexOf("->") + 2);
		String actuRetExprTypes = "";
		if (procOP.procBody.returnExpressions != null) {
			actuRetExprTypes = procOP.procBody.returnExpressions.stream().map(exprNode -> exprNode.type)
					.collect(Collectors.joining(","));
		}
		typeUtils.assertCanAssign(actuRetExprTypes, formalRetExprType);
		return null;
	}

	@Override
	public Object visitIfOP(IfOP ifOP) {
		super.visitIfOP(ifOP);
		if (!typeUtils.isBool(ifOP.expr.type)) {
			throw new IllegalStateException("expression must be boolean");
		}
		return null;
	}

	@Override
	public Object visitElifOP(ElifOP elifOP) {
		super.visitElifOP(elifOP);
		if (!typeUtils.isBool(elifOP.expr.type)) {
			throw new IllegalStateException("expression must be boolean");
		}
		return null;
	}

	@Override
	public Object visitWhileOP(WhileOP whileOP) {
		super.visitWhileOP(whileOP);
		if (!typeUtils.isBool(whileOP.expr.type)) {
			throw new IllegalStateException("expression must be boolean");
		}
		return null;
	}

	@Override
	public Object visitAssignOP(AssignOP assignOP) {
		super.visitAssignOP(assignOP);
		// TODO: (i = 5) restituisce 5, una cosa a cui pensare...
		String idsType = assignOP.ids.stream().map(id -> id.type).collect(Collectors.joining(","));
		String exprsType = assignOP.exprs.stream().map(exprNode -> exprNode.type).collect(Collectors.joining(","));
		typeUtils.assertCanAssign(exprsType, idsType);
		return null;
	}

	@Override
	public Object visitReadlnOP(ReadlnOP readlnOP) {
		// TODO: only certain types?
		return null;
	}

	@Override
	public Object visitWriteOP(WriteOP writeOP) {
		for (ExprNode expr : writeOP.exprs) {
			expr.accept(this);
			typeUtils.assertCanWrite(expr.type);
		}
		return null;
	}

	@Override
	public Object visitId(Id id) {
		id.type = scopingTable.getTypeOfCloser(id.name);
		return null;
	}

	// CONSTANTS
	@Override
	public Object visitNull(Null aNull) {
		// TODO: NULL type, what?
		return "null";
	}

	@Override
	public Object visitTrue(True aTrue) {
		aTrue.type = typeUtils.getBoolType();
		return null;
	}

	@Override
	public Object visitFalse(False aFalse) {
		aFalse.type = typeUtils.getBoolType();
		return null;
	}

	@Override
	public Object visitIntConst(IntConst intConst) {
		intConst.type = typeUtils.getIntType();
		return null;
	}

	@Override
	public Object visitFloatConst(FloatConst floatConst) {
		floatConst.type = typeUtils.getFloatType();
		return null;
	}

	@Override
	public Object visitStringConst(StringConst stringConst) {
		stringConst.type = typeUtils.getStringType();
		return null;
	}

	// BOOLEAN UNARY OPERATOR
	@Override
	public Object visitNotOP(NotOP notOP) {
		super.visitNotOP(notOP);
		typeUtils.assertCanNegate(notOP.expr.type);
		notOP.type = typeUtils.getBoolType();
		return null;
	}

	// ARITHMETIC UNARY OPERATOR
	@Override
	public Object visitUMinusOP(UMinusOP uMinusOP) {
		super.visitUMinusOP(uMinusOP);
		uMinusOP.type = typeUtils.assertUminusAndGetType(uMinusOP.expr.type);
		return null;
	}

	@Override
	public Object visitCallProcOP(CallProcOP callProcOP) {
		super.visitCallProcOP(callProcOP);
		// formal params types
		String procType = scopingTable.getTypeOfCloser(callProcOP.procId.name);
		String formalParamsType = procType.substring(0, procType.indexOf("->"));
		// actual params types
		String actuParamsType = "";
		if (callProcOP.exprs != null) {
			actuParamsType = callProcOP.exprs.stream().map(exprNode -> exprNode.type).collect(Collectors.joining(","));
		}
		typeUtils.assertCanAssign(actuParamsType, formalParamsType);
		callProcOP.type = procType.substring(procType.indexOf("->") + 2);
		return null;
	}

	// COMPARISON OPERATIONS
	private Object visitComparisionOperation(BinaryOperationNode bon) {
		bon.a.accept(this);
		bon.b.accept(this);
		typeUtils.assertCanCompare(bon.a.type, bon.b.type);
		bon.type = typeUtils.getBoolType();
		return null;
	}

	@Override
	public Object visitLTOP(LTOP ltop) {
		return visitComparisionOperation(ltop);
	}

	@Override
	public Object visitLEOP(LEOP leop) {
		return visitComparisionOperation(leop);
	}

	@Override
	public Object visitGTOP(GTOP gtop) {
		return visitComparisionOperation(gtop);
	}

	@Override
	public Object visitGEOP(GEOP geop) {
		return visitComparisionOperation(geop);
	}

	@Override
	public Object visitEQOP(EQOP eqop) {
		return visitComparisionOperation(eqop);
	}

	// BOOLEAN BINARY OPERATIONS
	private Object visitBooleanBinaryOperation(BinaryOperationNode bon) {
		bon.a.accept(this);
		bon.b.accept(this);
//		if (bon.a.type.isBoolean() && bon.b.type.isBoolean()) {
		if (typeUtils.isBool(bon.a.type) && typeUtils.isBool(bon.b.type)) {
			bon.type = typeUtils.getBoolType();
			return null;
		} else {
			throw new IllegalStateException("set some conversion tables...");
		}
	}

	@Override
	public Object visitAndOP(AndOP andOP) {
		return visitBooleanBinaryOperation(andOP);
	}

	@Override
	public Object visitOrOP(OrOP orOP) {
		return visitBooleanBinaryOperation(orOP);
	}

	// BINARY ARITHMETIC OPERATIONS
	private Object visitBinaryArithmeticOperation(BinaryOperationNode bon) {
		bon.a.accept(this);
		bon.b.accept(this);
		if (!typeUtils.isNumber(bon.a.type) || !typeUtils.isNumber(bon.b.type)) {
			throw new IllegalStateException("set some conversion tables...");
		} else if (typeUtils.isInt(bon.a.type) && typeUtils.isInt(bon.b.type)) {
			bon.type = typeUtils.getIntType();
		} else {
			bon.type = typeUtils.getFloatType();
		}
		return null;
	}

	@Override
	public Object visitTimesOP(TimesOP timesOP) {
		return visitBinaryArithmeticOperation(timesOP);
	}

	@Override
	public Object visitDivOP(DivOP divOP) {
		return visitBinaryArithmeticOperation(divOP);
	}

	@Override
	public Object visitPlusOP(PlusOP plusOP) {
		return visitBinaryArithmeticOperation(plusOP);
	}

	@Override
	public Object visitMinusOP(MinusOP minusOP) {
		return visitBinaryArithmeticOperation(minusOP);
	}

}
