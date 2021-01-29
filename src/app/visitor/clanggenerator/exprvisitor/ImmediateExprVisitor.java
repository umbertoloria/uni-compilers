package app.visitor.clanggenerator.exprvisitor;

import app.node.BinaryOperationNode;
import app.node.binop.*;
import app.node.expr.*;
import app.visitor.ExclusiveNodeVisitor;

public class ImmediateExprVisitor extends ExclusiveNodeVisitor<Boolean> {

	@Override
	public Boolean visitId(Id id) {
		return false;
	}

	@Override
	public Boolean visitTrue(True aTrue) {
		return true;
	}

	@Override
	public Boolean visitFalse(False aFalse) {
		return true;
	}

	@Override
	public Boolean visitIntConst(IntConst intConst) {
		return true;
	}

	@Override
	public Boolean visitFloatConst(FloatConst floatConst) {
		return true;
	}

	@Override
	public Boolean visitStringConst(StringConst stringConst) {
		return true;
	}

	@Override
	public Boolean visitNotOP(NotOP notOP) {
		return notOP.expr.accept(this);
	}

	@Override
	public Boolean visitUMinusOP(UMinusOP uMinusOP) {
		return uMinusOP.expr.accept(this);
	}

	@Override
	public Boolean visitCallProcOP(CallProcOP callProcOP) {
		return false;
	}

	@Override
	public Boolean visitLTOP(LTOP ltop) {
		return visitBinaryOperation(ltop);
	}

	@Override
	public Boolean visitLEOP(LEOP leop) {
		return visitBinaryOperation(leop);
	}

	@Override
	public Boolean visitGTOP(GTOP gtop) {
		return visitBinaryOperation(gtop);
	}

	@Override
	public Boolean visitGEOP(GEOP geop) {
		return visitBinaryOperation(geop);
	}

	@Override
	public Boolean visitEQOP(EQOP eqop) {
		return visitBinaryOperation(eqop);
	}

	@Override
	public Boolean visitAndOP(AndOP andOP) {
		return visitBinaryOperation(andOP);
	}

	@Override
	public Boolean visitOrOP(OrOP orOP) {
		return visitBinaryOperation(orOP);
	}

	@Override
	public Boolean visitTimesOP(TimesOP timesOP) {
		return visitBinaryOperation(timesOP);
	}

	@Override
	public Boolean visitDivOP(DivOP divOP) {
		return visitBinaryOperation(divOP);
	}

	@Override
	public Boolean visitPlusOP(PlusOP plusOP) {
		return visitBinaryOperation(plusOP);
	}

	@Override
	public Boolean visitMinusOP(MinusOP minusOP) {
		return visitBinaryOperation(minusOP);
	}

	private Boolean visitBinaryOperation(BinaryOperationNode bon) {
		return bon.a.accept(this) && bon.b.accept(this);
	}

}
