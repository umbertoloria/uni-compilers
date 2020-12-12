package app.visitor;

import app.node.*;
import app.node.binop.*;
import app.node.expr.*;
import app.node.stat.*;

public interface INodeVisitor {

	Object visitProgramOP(ProgramOP programOP);
	Object visitVarDeclOP(VarDeclOP varDeclOP);
	Object visitTypeNode(TypeNode typeNode);
	Object visitIdInitOP(IdInitOP idInitOP);

	Object visitProcOP(ProcOP procOP);
	Object visitParDeclOP(ParDeclOP parDeclOP);
	Object visitResultTypeNode(ResultTypeNode resultTypeNode);
	Object visitProcBodyOP(ProcBodyOP procBodyOP);

	Object visitBodyOP(BodyOP bodyOP);
	Object visitIfOP(IfOP ifOP);
	Object visitElifOP(ElifOP elifOP);
	Object visitWhileOP(WhileOP whileOP);
	Object visitAssignOP(AssignOP assignOP);
	Object visitReadlnOP(ReadlnOP readlnOP);
	Object visitWriteOP(WriteOP writeOP);

	Object visitId(Id id);
	Object visitNull(Null aNull);
	Object visitTrue(True aTrue);
	Object visitFalse(False aFalse);
	Object visitIntConst(IntConst intConst);
	Object visitFloatConst(FloatConst floatConst);
	Object visitStringConst(StringConst stringConst);

	Object visitNotOP(NotOP notOP);
	Object visitUMinusOP(UMinusOP uMinusOP);
	Object visitCallProcOP(CallProcOP callProcOP);

	Object visitLTOP(LTOP ltop);
	Object visitLEOP(LEOP leop);
	Object visitGTOP(GTOP gtop);
	Object visitGEOP(GEOP geop);
	Object visitEQOP(EQOP eqop);

	Object visitAndOP(AndOP andOP);
	Object visitOrOP(OrOP orOP);

	Object visitTimesOP(TimesOP timesOP);
	Object visitDivOP(DivOP divOP);
	Object visitPlusOP(PlusOP plusOP);
	Object visitMinusOP(MinusOP minusOP);

}
