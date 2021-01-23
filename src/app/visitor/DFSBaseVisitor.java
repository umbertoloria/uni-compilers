package app.visitor;

import app.node.*;
import app.node.binop.*;
import app.node.expr.*;
import app.node.stat.*;

public class DFSBaseVisitor implements INodeVisitor {

	@Override
	public Object visitProgramOP(ProgramOP programOP) {
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
	public Object visitVarDeclOP(VarDeclOP varDeclOP) {
		for (IdInitOP idInit : varDeclOP.idInits) {
			if (idInit.expr != null) {
				idInit.expr.accept(this);
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
		if (procOP.procBody != null) {
			procOP.procBody.accept(this);
		}
		return null;
	}

	@Override
	public Object visitParDeclOP(ParDeclOP parDeclOP) {
		return null;
	}

	@Override
	public Object visitProcBodyOP(ProcBodyOP procBodyOP) {
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
	public Object visitBodyOP(BodyOP bodyOP) {
		for (StatNode stmt : bodyOP.stmts) {
			stmt.accept(this);
		}
		return null;
	}

	@Override
	public Object visitIfOP(IfOP ifOP) {
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
	public Object visitElifOP(ElifOP elifOP) {
		elifOP.expr.accept(this);
		elifOP.body.accept(this);
		return null;
	}

	@Override
	public Object visitWhileOP(WhileOP whileOP) {
		if (whileOP.preStmts != null) {
			whileOP.preStmts.accept(this);
		}
		whileOP.expr.accept(this);
		whileOP.iterStmts.accept(this);
		return null;
	}

	@Override
	public Object visitAssignOP(AssignOP assignOP) {
		for (Id id : assignOP.ids) {
			id.accept(this);
		}
		for (ExprNode expr : assignOP.exprs) {
			expr.accept(this);
		}
		return null;
	}

	@Override
	public Object visitReadlnOP(ReadlnOP readlnOP) {
		for (Id id : readlnOP.ids) {
			id.accept(this);
		}
		return null;
	}

	@Override
	public Object visitWriteOP(WriteOP writeOP) {
		for (ExprNode expr : writeOP.exprs) {
			expr.accept(this);
		}
		return null;
	}

	@Override
	public Object visitId(Id id) {
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
		notOP.expr.accept(this);
		return null;
	}

	@Override
	public Object visitUMinusOP(UMinusOP uMinusOP) {
		uMinusOP.expr.accept(this);
		return null;
	}

	@Override
	public Object visitCallProcOP(CallProcOP callProcOP) {
		// TODO: problema: a volte ID lo devi vedere, ma adesso no... poco coesa
		if (callProcOP.exprs != null) {
			for (ExprNode expr : callProcOP.exprs) {
				expr.accept(this);
			}
		}
		return null;
	}

	@Override
	public Object visitLTOP(LTOP ltop) {
		ltop.a.accept(this);
		ltop.b.accept(this);
		return null;
	}

	@Override
	public Object visitLEOP(LEOP leop) {
		leop.a.accept(this);
		leop.b.accept(this);
		return null;
	}

	@Override
	public Object visitGTOP(GTOP gtop) {
		gtop.a.accept(this);
		gtop.b.accept(this);
		return null;
	}

	@Override
	public Object visitGEOP(GEOP geop) {
		geop.a.accept(this);
		geop.b.accept(this);
		return null;
	}

	@Override
	public Object visitEQOP(EQOP eqop) {
		eqop.a.accept(this);
		eqop.b.accept(this);
		return null;
	}

	@Override
	public Object visitAndOP(AndOP andOP) {
		andOP.a.accept(this);
		andOP.b.accept(this);
		return null;
	}

	@Override
	public Object visitOrOP(OrOP orOP) {
		orOP.a.accept(this);
		orOP.b.accept(this);
		return null;
	}

	@Override
	public Object visitTimesOP(TimesOP timesOP) {
		timesOP.a.accept(this);
		timesOP.b.accept(this);
		return null;
	}

	@Override
	public Object visitDivOP(DivOP divOP) {
		divOP.a.accept(this);
		divOP.b.accept(this);
		return null;
	}

	@Override
	public Object visitPlusOP(PlusOP plusOP) {
		plusOP.a.accept(this);
		plusOP.b.accept(this);
		return null;
	}

	@Override
	public Object visitMinusOP(MinusOP minusOP) {
		minusOP.a.accept(this);
		minusOP.b.accept(this);
		return null;
	}

}
