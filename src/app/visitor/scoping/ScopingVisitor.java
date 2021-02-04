package app.visitor.scoping;

import app.node.*;
import app.node.expr.CallProcOP;
import app.node.expr.Id;
import app.node.stat.RepeatOP;
import app.visitor.DFSBaseVisitor;

public class ScopingVisitor extends DFSBaseVisitor<Object> {

	private ScopingTable scopingTable;

	@Override
	public Object visitProgramOP(ProgramOP programOP) {
		scopingTable = new ScopingTable("global");
		super.visitProgramOP(programOP);
		scopingTable.resolveUnmatchedUsages();
		return scopingTable;
	}

	@Override
	public Object visitVarDeclOP(VarDeclOP varDeclOP) {
		for (IdInitOP idInit : varDeclOP.idInits) {
			// Prima si valida 'expr' e poi si dichiara la variabile 'idInit.id.name' perché altrimenti potrebbe già
			// essere usata da 'expr', il ché sarebbe un errore.
			if (idInit.expr != null) {
				idInit.expr.accept(this);
			}
			scopingTable.declareVariable(idInit.id.name, varDeclOP.type.getStringType());
		}
		return null;
	}

	@Override
	public Object visitProcOP(ProcOP procOP) {
		// Dichiarazione funzione
		StringBuilder procedureType = new StringBuilder();
		if (procOP.parDecls != null) {
			for (ParDeclOP parDecl : procOP.parDecls) {
				procedureType.append((parDecl.type.getStringType() + ",").repeat(parDecl.ids.size()));
			}
			procedureType.deleteCharAt(procedureType.length() - 1);
		}
		procedureType.append("->");
		if (procOP.returnTypes != null) {
			for (TypeNode returnType : procOP.returnTypes) {
				procedureType.append(returnType.getStringType());
				procedureType.append(',');
			}
			procedureType.deleteCharAt(procedureType.length() - 1);
		}
		scopingTable.declareProcedure(procOP.id.name, procedureType.toString());
		// Nuovo scope
		scopingTable = scopingTable.createChild(procOP.id.name);
		if (procOP.parDecls != null) {
			for (ParDeclOP parDecl : procOP.parDecls) {
				parDecl.accept(this);
			}
		}
		procOP.procBody.accept(this);
		scopingTable = scopingTable.getParent();
		return null;
	}

	@Override
	public Object visitParDeclOP(ParDeclOP parDeclOP) {
		for (Id id : parDeclOP.ids) {
			scopingTable.declareVariable(id.name, parDeclOP.type.getStringType());
		}
		return null;
	}

	@Override
	public Object visitRepeatOP(RepeatOP repeatOP) {
		scopingTable = scopingTable.createChild(repeatOP.name);
		for (VarDeclOP varDecl : repeatOP.varDecls) {
			varDecl.accept(this);
		}
		repeatOP.stmts.accept(this);
		scopingTable = scopingTable.getParent();
		repeatOP.expr.accept(this);
		return null;
	}

	@Override
	public Object visitId(Id id) {
		scopingTable.useVariable(id.name);
		return null;
	}

	@Override
	public Object visitCallProcOP(CallProcOP callProcOP) {
		scopingTable.useProcedure(callProcOP.procId.name);
		if (callProcOP.exprs != null) {
			for (ExprNode expr : callProcOP.exprs) {
				expr.accept(this);
			}
		}
		return null;
	}

}
