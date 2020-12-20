package app.node;

import app.Node;
import app.node.expr.Id;
import app.visitor.INodeVisitor;

import java.util.List;

public class ParDeclOP extends Node {

	private TypeNode type;
	private List<Id> ids;

	public ParDeclOP(TypeNode type, List<Id> ids) {
		this.type = type;
		if (type == null) {
			throw new IllegalStateException();
		}
		this.ids = ids;
		if (ids == null || ids.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	public TypeNode getType() {
		return type;
	}

	public List<Id> getIds() {
		return ids;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitParDeclOP(this);
	}

}
