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
		this.parDecls = parDecls;
		this.returnTypes = returnTypes;
		this.procBody = procBody;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitProcOP(this);
	}

}
