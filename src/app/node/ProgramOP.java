package app.node;

import app.Driver;
import app.Node;
import app.visitor.INodeVisitor;

import java.util.List;

public class ProgramOP extends Node {

	private List<VarDeclOP> varDecls;
	private List<ProcOP> procs;

	public ProgramOP(List<VarDeclOP> varDecls, List<ProcOP> procs) {
		this.varDecls = varDecls;
		this.procs = procs;
		if (procs == null || procs.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	public List<VarDeclOP> getVarDecls() {
		return varDecls;
	}

	public List<ProcOP> getProcs() {
		return procs;
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "ProgramOP");
		System.out.println("    ".repeat(level + 1) + "VarDeclOP list");
		Driver.visit(varDecls, level + 1);
		System.out.println("    ".repeat(level + 1) + "ProcOP list");
		Driver.visit(procs, level + 1);
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitProgramOP(this);
	}

}
