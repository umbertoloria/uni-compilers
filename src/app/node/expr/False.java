package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class False extends ExprNode {

	public String toString() {
		return "<False>";
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitFalse(this);
	}

}
