package app.node;

import app.Node;
import app.node.expr.Id;
import app.visitor.INodeVisitor;

import java.util.List;

public class ProcOP extends Node {

	public final Id id;
	public final List<ParDeclOP> parDecls;
	public final List<TypeNode> returnTypes;
	public final ProcBodyOP procBody;

	public ProcOP(Id id, List<ParDeclOP> parDecls, List<TypeNode> returnTypes, ProcBodyOP procBody) {
		this.id = id;
		if (id == null) {
			throw new IllegalStateException();
		}
		this.parDecls = parDecls;
		this.returnTypes = returnTypes;
		this.procBody = procBody;
		if (procBody == null) {
			throw new IllegalStateException();
		}
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitProcOP(this);
	}

	@Override
	public String toString() {
		return "ProcOP{" + id.name + ", p=" + parDecls + ", r=" + returnTypes + "}";
	}

}
