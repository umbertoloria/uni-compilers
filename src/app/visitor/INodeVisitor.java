package app.visitor;

import app.node.*;
import app.node.binop.*;
import app.node.expr.*;
import app.node.stat.*;

public interface INodeVisitor<T> {

	T visitProgramOP(ProgramOP programOP);
	T visitVarDeclOP(VarDeclOP varDeclOP);
	T visitTypeNode(TypeNode typeNode);
	T visitIdInitOP(IdInitOP idInitOP);

	T visitProcOP(ProcOP procOP);
	T visitParDeclOP(ParDeclOP parDeclOP);
	T visitProcBodyOP(ProcBodyOP procBodyOP);

	T visitBodyOP(BodyOP bodyOP);
	T visitIfOP(IfOP ifOP);
	T visitElifOP(ElifOP elifOP);
	T visitWhileOP(WhileOP whileOP);
	T visitRepeatOP(RepeatOP repeatOP);
	T visitAssignOP(AssignOP assignOP);
	T visitReadlnOP(ReadlnOP readlnOP);
	T visitWriteOP(WriteOP writeOP);
	T visitCallProcStatOP(CallProcStatOP callProcStatOP);

	T visitId(Id id);
	T visitTrue(True aTrue);
	T visitFalse(False aFalse);
	T visitIntConst(IntConst intConst);
	T visitFloatConst(FloatConst floatConst);
	T visitStringConst(StringConst stringConst);

	T visitNotOP(NotOP notOP);
	T visitUMinusOP(UMinusOP uMinusOP);
	T visitCallProcOP(CallProcOP callProcOP);

	T visitLTOP(LTOP ltop);
	T visitLEOP(LEOP leop);
	T visitGTOP(GTOP gtop);
	T visitGEOP(GEOP geop);
	T visitEQOP(EQOP eqop);

	T visitAndOP(AndOP andOP);
	T visitOrOP(OrOP orOP);

	T visitTimesOP(TimesOP timesOP);
	T visitDivOP(DivOP divOP);
	T visitPlusOP(PlusOP plusOP);
	T visitMinusOP(MinusOP minusOP);

}
