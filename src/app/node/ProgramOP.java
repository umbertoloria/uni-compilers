package app.node;

import app.Node;
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
	public Object accept(INodeVisitor visitor) {
		return visitor.visitProgramOP(this);
	}

}
