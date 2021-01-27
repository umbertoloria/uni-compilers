package app.visitor.typechecking;

import app.node.*;
import app.node.binop.*;
import app.node.expr.*;
import app.node.stat.*;
import app.visitor.DFSBaseVisitor;
import app.visitor.ErrorsManager;
import app.visitor.scoping.ScopingTable;

import java.util.stream.Collectors;

public class TypeCheckingVisitor extends DFSBaseVisitor<Void> {

	private ScopingTable scopingTable;
	private TypeUtils typeUtils = new TypeUtils();
	private ErrorsManager errorsManager = new ErrorsManager();

	public TypeCheckingVisitor(ScopingTable scopingTable) {
		this.scopingTable = scopingTable;
	}

	@Override
	public Void visitVarDeclOP(VarDeclOP varDeclOP) {
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
	public Void visitProcOP(ProcOP procOP) {
		scopingTable = scopingTable.getScopingTableOf(procOP.id.name);
		procOP.procBody.accept(this);
		scopingTable = scopingTable.getParent();
		// Type-matching tra espressioni formali e attuali
		String procType = scopingTable.getTypeInCloserScopeGoingUp(procOP.id.name);
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
	public Void visitIfOP(IfOP ifOP) {
		super.visitIfOP(ifOP);
		if (!typeUtils.isBool(ifOP.expr.type)) {
			errorsManager.typeMismatchInCondition(ifOP.expr.type);
		}
		return null;
	}

	@Override
	public Void visitElifOP(ElifOP elifOP) {
		super.visitElifOP(elifOP);
		if (!typeUtils.isBool(elifOP.expr.type)) {
			errorsManager.typeMismatchInCondition(elifOP.expr.type);
		}
		return null;
	}

	@Override
	public Void visitWhileOP(WhileOP whileOP) {
		super.visitWhileOP(whileOP);
		if (!typeUtils.isBool(whileOP.expr.type)) {
			errorsManager.typeMismatchInCondition(whileOP.expr.type);
		}
		return null;
	}

	@Override
	public Void visitAssignOP(AssignOP assignOP) {
		super.visitAssignOP(assignOP);
		String idsType = assignOP.ids.stream().map(id -> id.type).collect(Collectors.joining(","));
		String exprsType = assignOP.exprs.stream().map(exprNode -> exprNode.type).collect(Collectors.joining(","));
		typeUtils.assertCanAssign(exprsType, idsType);
		return null;
	}

	@Override
	public Void visitReadlnOP(ReadlnOP readlnOP) {
		for (Id id : readlnOP.ids) {
			id.accept(this);
		}
		return null;
	}

	@Override
	public Void visitWriteOP(WriteOP writeOP) {
		for (ExprNode expr : writeOP.exprs) {
			expr.accept(this);
		}
		return null;
	}

	@Override
	public Void visitCallProcStatOP(CallProcStatOP callProcStatOP) {
		callProcStatOP.callProcOP.accept(this);
		return null;
	}

	@Override
	public Void visitId(Id id) {
		id.type = scopingTable.getTypeInCloserScopeGoingUp(id.name);
		return null;
	}

	// CONSTANTS
	@Override
	public Void visitNull(Null aNull) {
		// TODO: NULL type, what?
		throw new IllegalStateException("IDK");
	}

	@Override
	public Void visitTrue(True aTrue) {
		aTrue.type = typeUtils.getBoolType();
		return null;
	}

	@Override
	public Void visitFalse(False aFalse) {
		aFalse.type = typeUtils.getBoolType();
		return null;
	}

	@Override
	public Void visitIntConst(IntConst intConst) {
		intConst.type = typeUtils.getIntType();
		return null;
	}

	@Override
	public Void visitFloatConst(FloatConst floatConst) {
		floatConst.type = typeUtils.getFloatType();
		return null;
	}

	@Override
	public Void visitStringConst(StringConst stringConst) {
		stringConst.type = typeUtils.getStringType();
		return null;
	}

	// BOOLEAN UNARY OPERATOR
	@Override
	public Void visitNotOP(NotOP notOP) {
		super.visitNotOP(notOP);
		typeUtils.assertCanNegate(notOP.expr.type);
		notOP.type = typeUtils.getBoolType();
		return null;
	}

	// ARITHMETIC UNARY OPERATOR
	@Override
	public Void visitUMinusOP(UMinusOP uMinusOP) {
		super.visitUMinusOP(uMinusOP);
		uMinusOP.type = typeUtils.assertUminusAndGetType(uMinusOP.expr.type);
		return null;
	}

	@Override
	public Void visitCallProcOP(CallProcOP callProcOP) {
		super.visitCallProcOP(callProcOP);
		// formal params types
		String procType = scopingTable.getTypeInCloserScopeGoingUp(callProcOP.procId.name);
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
	private Void visitComparisionOperation(BinaryOperationNode bon) {
		bon.a.accept(this);
		bon.b.accept(this);
		typeUtils.assertCanCompare(bon.a.type, bon.b.type);
		bon.type = typeUtils.getBoolType();
		return null;
	}

	@Override
	public Void visitLTOP(LTOP ltop) {
		return visitComparisionOperation(ltop);
	}

	@Override
	public Void visitLEOP(LEOP leop) {
		return visitComparisionOperation(leop);
	}

	@Override
	public Void visitGTOP(GTOP gtop) {
		return visitComparisionOperation(gtop);
	}

	@Override
	public Void visitGEOP(GEOP geop) {
		return visitComparisionOperation(geop);
	}

	@Override
	public Void visitEQOP(EQOP eqop) {
		return visitComparisionOperation(eqop);
	}

	// BINARY BOOLEAN OPERATIONS
	private Void visitBinaryBooleanOperation(BinaryOperationNode bon) {
		bon.a.accept(this);
		bon.b.accept(this);
		if (typeUtils.isBool(bon.a.type) && typeUtils.isBool(bon.b.type)) {
			bon.type = typeUtils.getBoolType();
		} else {
			errorsManager.typeMismatchInBinaryBooleanOperation(bon.a.type, bon.b.type);
		}
		return null;
	}

	@Override
	public Void visitAndOP(AndOP andOP) {
		return visitBinaryBooleanOperation(andOP);
	}

	@Override
	public Void visitOrOP(OrOP orOP) {
		return visitBinaryBooleanOperation(orOP);
	}

	// BINARY ARITHMETIC OPERATIONS
	private Void visitBinaryArithmeticOperation(BinaryOperationNode bon) {
		bon.a.accept(this);
		bon.b.accept(this);
		if (!typeUtils.isNumber(bon.a.type) || !typeUtils.isNumber(bon.b.type)) {
			errorsManager.typeMismatchInBinaryArithmeticOperation(bon.a.type, bon.b.type);
		} else if (typeUtils.isInt(bon.a.type) && typeUtils.isInt(bon.b.type)) {
			bon.type = typeUtils.getIntType();
		} else {
			bon.type = typeUtils.getFloatType();
		}
		return null;
	}

	@Override
	public Void visitTimesOP(TimesOP timesOP) {
		return visitBinaryArithmeticOperation(timesOP);
	}

	@Override
	public Void visitDivOP(DivOP divOP) {
		return visitBinaryArithmeticOperation(divOP);
	}

	@Override
	public Void visitPlusOP(PlusOP plusOP) {
		return visitBinaryArithmeticOperation(plusOP);
	}

	@Override
	public Void visitMinusOP(MinusOP minusOP) {
		return visitBinaryArithmeticOperation(minusOP);
	}

}
