package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class False extends ExprNode {

	public String toString() {
		return "<False>";
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitFalse(this);
	}

}
