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

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "ParDeclOP");
		type.visit(level + 1);
		System.out.println("    ".repeat(level + 1) + "Ids");
		System.out.println("    ".repeat(level + 2) + ids.toString());
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitParDeclOP(this);
	}

}
