package app.visitor;

import app.node.*;
import app.node.binop.*;
import app.node.expr.*;
import app.node.stat.*;

public class ExclusiveNodeVisitor<T> implements INodeVisitor<T> {

	@Override
	public T visitProgramOP(ProgramOP programOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitVarDeclOP(VarDeclOP varDeclOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitTypeNode(TypeNode typeNode) {
		throw new IllegalStateException();
	}

	@Override
	public T visitIdInitOP(IdInitOP idInitOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitProcOP(ProcOP procOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitParDeclOP(ParDeclOP parDeclOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitProcBodyOP(ProcBodyOP procBodyOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitBodyOP(BodyOP bodyOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitIfOP(IfOP ifOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitElifOP(ElifOP elifOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitWhileOP(WhileOP whileOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitAssignOP(AssignOP assignOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitReadlnOP(ReadlnOP readlnOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitWriteOP(WriteOP writeOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitCallProcStatOP(CallProcStatOP callProcStatOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitId(Id id) {
		throw new IllegalStateException();
	}

	@Override
	public T visitNull(Null aNull) {
		throw new IllegalStateException();
	}

	@Override
	public T visitTrue(True aTrue) {
		throw new IllegalStateException();
	}

	@Override
	public T visitFalse(False aFalse) {
		throw new IllegalStateException();
	}

	@Override
	public T visitIntConst(IntConst intConst) {
		throw new IllegalStateException();
	}

	@Override
	public T visitFloatConst(FloatConst floatConst) {
		throw new IllegalStateException();
	}

	@Override
	public T visitStringConst(StringConst stringConst) {
		throw new IllegalStateException();
	}

	@Override
	public T visitNotOP(NotOP notOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitUMinusOP(UMinusOP uMinusOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitCallProcOP(CallProcOP callProcOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitLTOP(LTOP ltop) {
		throw new IllegalStateException();
	}

	@Override
	public T visitLEOP(LEOP leop) {
		throw new IllegalStateException();
	}

	@Override
	public T visitGTOP(GTOP gtop) {
		throw new IllegalStateException();
	}

	@Override
	public T visitGEOP(GEOP geop) {
		throw new IllegalStateException();
	}

	@Override
	public T visitEQOP(EQOP eqop) {
		throw new IllegalStateException();
	}

	@Override
	public T visitAndOP(AndOP andOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitOrOP(OrOP orOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitTimesOP(TimesOP timesOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitDivOP(DivOP divOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitPlusOP(PlusOP plusOP) {
		throw new IllegalStateException();
	}

	@Override
	public T visitMinusOP(MinusOP minusOP) {
		throw new IllegalStateException();
	}

}
