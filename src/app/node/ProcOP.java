package app.node;

import app.Node;
import app.node.expr.Id;
import app.visitor.INodeVisitor;

import java.util.List;

public class ProcOP extends Node {

	private Id id;
	private List<ParDeclOP> parDecls;
	private List<ResultTypeNode> returnTypes;
	private ProcBodyOP procBody;

	public ProcOP(Id id, List<ParDeclOP> parDecls, List<ResultTypeNode> returnTypes, ProcBodyOP procBody) {
		this.id = id;
		this.parDecls = parDecls;
		this.returnTypes = returnTypes;
		this.procBody = procBody;
		if (returnTypes == null || returnTypes.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	public Id getId() {
		return id;
	}

	public List<ParDeclOP> getParDecls() {
		return parDecls;
	}

	public List<ResultTypeNode> getReturnTypes() {
		return returnTypes;
	}

	public ProcBodyOP getProcBody() {
		return procBody;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitProcOP(this);
	}

}
