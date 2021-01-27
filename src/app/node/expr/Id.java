package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class Id extends ExprNode {

	public final String name;

	public Id(String name) {
		this.name = name;
		if (name == null || name.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitId(this);
	}

	public String toString() {
		return "<ID, " + name + ">";
	}

}
