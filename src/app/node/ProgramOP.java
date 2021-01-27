package app.node;

import app.visitor.INodeVisitor;

import java.util.List;

public class ProgramOP extends Node {

	public final List<VarDeclOP> varDecls;
	public final List<ProcOP> procs;

	public ProgramOP(List<VarDeclOP> varDecls, List<ProcOP> procs) {
		this.varDecls = varDecls;
		this.procs = procs;
		if (procs == null || procs.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitProgramOP(this);
	}

}
