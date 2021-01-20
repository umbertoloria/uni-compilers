package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class Id extends ExprNode {

	private String name;

	public Id(String name) {
		this.name = name;
		if (name == null || name.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	public String getName() {
		return name;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitId(this);
	}

	public String toString() {
		return "<ID, " + name + ">";
	}

}
