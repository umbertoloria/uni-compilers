package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class NotOP extends ExprNode {

	private ExprNode expr;

	public NotOP(ExprNode expr) {
		this.expr = expr;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitNotOP(this);
	}

	public String toString() {
		return "Not [" + expr + "]";
	}

}
