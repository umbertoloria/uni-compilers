package app.visitor;

import app.node.*;
import app.node.binop.*;
import app.node.expr.*;
import app.node.stat.*;

public class DFSBaseVisitor<T> implements INodeVisitor<T> {

	@Override
	public T visitProgramOP(ProgramOP programOP) {
		if (programOP.varDecls != null) {
			for (VarDeclOP varDecl : programOP.varDecls) {
				varDecl.accept(this);
			}
		}
		for (ProcOP proc : programOP.procs) {
			proc.accept(this);
		}
		return null;
	}

	@Override
	public T visitVarDeclOP(VarDeclOP varDeclOP) {
		for (IdInitOP idInit : varDeclOP.idInits) {
			if (idInit.expr != null) {
				idInit.expr.accept(this);
			}
		}
		return null;
	}

	@Override
	public T visitTypeNode(TypeNode typeNode) {
		return null;
	}

	@Override
	public T visitIdInitOP(IdInitOP idInitOP) {
		return null;
	}

	@Override
	public T visitProcOP(ProcOP procOP) {
		if (procOP.parDecls != null) {
			for (ParDeclOP parDecl : procOP.parDecls) {
				parDecl.accept(this);
			}
		}
		if (procOP.returnTypes != null) {
			for (TypeNode returnType : procOP.returnTypes) {
				returnType.accept(this);
			}
		}
		procOP.procBody.accept(this);
		return null;
	}

	@Override
	public T visitParDeclOP(ParDeclOP parDeclOP) {
		return null;
	}

	@Override
	public T visitProcBodyOP(ProcBodyOP procBodyOP) {
		if (procBodyOP.varDecls != null) {
			for (VarDeclOP varDecl : procBodyOP.varDecls) {
				varDecl.accept(this);
			}
		}
		if (procBodyOP.body != null) {
			procBodyOP.body.accept(this);
		}
		if (procBodyOP.returnExpressions != null) {
			for (ExprNode returnExpression : procBodyOP.returnExpressions) {
				returnExpression.accept(this);
			}
		}
		return null;
	}

	@Override
	public T visitBodyOP(BodyOP bodyOP) {
		for (StatNode stmt : bodyOP.stmts) {
			stmt.accept(this);
		}
		return null;
	}

	@Override
	public T visitIfOP(IfOP ifOP) {
		ifOP.expr.accept(this);
		ifOP.ifBody.accept(this);
		if (ifOP.elifs != null) {
			for (ElifOP elif : ifOP.elifs) {
				elif.accept(this);
			}
		}
		if (ifOP.elseBody != null) {
			ifOP.elseBody.accept(this);
		}
		return null;
	}

	@Override
	public T visitElifOP(ElifOP elifOP) {
		elifOP.expr.accept(this);
		elifOP.body.accept(this);
		return null;
	}

	@Override
	public T visitWhileOP(WhileOP whileOP) {
		if (whileOP.preStmts != null) {
			whileOP.preStmts.accept(this);
		}
		whileOP.expr.accept(this);
		whileOP.iterStmts.accept(this);
		return null;
	}

	@Override
	public T visitRepeatOP(RepeatOP repeatOP) {
		for (VarDeclOP varDecl : repeatOP.varDecls) {
			varDecl.accept(this);
		}
		repeatOP.stmts.accept(this);
		repeatOP.expr.accept(this);
		return null;
	}

	@Override
	public T visitAssignOP(AssignOP assignOP) {
		for (Id id : assignOP.ids) {
			id.accept(this);
		}
		for (ExprNode expr : assignOP.exprs) {
			expr.accept(this);
		}
		return null;
	}

	@Override
	public T visitReadlnOP(ReadlnOP readlnOP) {
		for (Id id : readlnOP.ids) {
			id.accept(this);
		}
		return null;
	}

	@Override
	public T visitWriteOP(WriteOP writeOP) {
		for (ExprNode expr : writeOP.exprs) {
			expr.accept(this);
		}
		return null;
	}

	@Override
	public T visitCallProcStatOP(CallProcStatOP callProcStatOP) {
		return callProcStatOP.callProcOP.accept(this);
	}

	@Override
	public T visitId(Id id) {
		return null;
	}

	@Override
	public T visitTrue(True aTrue) {
		return null;
	}

	@Override
	public T visitFalse(False aFalse) {
		return null;
	}

	@Override
	public T visitIntConst(IntConst intConst) {
		return null;
	}

	@Override
	public T visitFloatConst(FloatConst floatConst) {
		return null;
	}

	@Override
	public T visitStringConst(StringConst stringConst) {
		return null;
	}

	@Override
	public T visitNotOP(NotOP notOP) {
		notOP.expr.accept(this);
		return null;
	}

	@Override
	public T visitUMinusOP(UMinusOP uMinusOP) {
		uMinusOP.expr.accept(this);
		return null;
	}

	@Override
	public T visitCallProcOP(CallProcOP callProcOP) {
		if (callProcOP.exprs != null) {
			for (ExprNode expr : callProcOP.exprs) {
				expr.accept(this);
			}
		}
		return null;
	}

	@Override
	public T visitLTOP(LTOP ltop) {
		ltop.a.accept(this);
		ltop.b.accept(this);
		return null;
	}

	@Override
	public T visitLEOP(LEOP leop) {
		leop.a.accept(this);
		leop.b.accept(this);
		return null;
	}

	@Override
	public T visitGTOP(GTOP gtop) {
		gtop.a.accept(this);
		gtop.b.accept(this);
		return null;
	}

	@Override
	public T visitGEOP(GEOP geop) {
		geop.a.accept(this);
		geop.b.accept(this);
		return null;
	}

	@Override
	public T visitEQOP(EQOP eqop) {
		eqop.a.accept(this);
		eqop.b.accept(this);
		return null;
	}

	@Override
	public T visitAndOP(AndOP andOP) {
		andOP.a.accept(this);
		andOP.b.accept(this);
		return null;
	}

	@Override
	public T visitOrOP(OrOP orOP) {
		orOP.a.accept(this);
		orOP.b.accept(this);
		return null;
	}

	@Override
	public T visitTimesOP(TimesOP timesOP) {
		timesOP.a.accept(this);
		timesOP.b.accept(this);
		return null;
	}

	@Override
	public T visitDivOP(DivOP divOP) {
		divOP.a.accept(this);
		divOP.b.accept(this);
		return null;
	}

	@Override
	public T visitPlusOP(PlusOP plusOP) {
		plusOP.a.accept(this);
		plusOP.b.accept(this);
		return null;
	}

	@Override
	public T visitMinusOP(MinusOP minusOP) {
		minusOP.a.accept(this);
		minusOP.b.accept(this);
		return null;
	}

}
