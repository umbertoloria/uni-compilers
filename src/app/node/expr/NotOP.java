package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class NotOP extends ExprNode {

	public final ExprNode expr;

	public NotOP(ExprNode expr) {
		this.expr = expr;
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitNotOP(this);
	}

	public String toString() {
		return "Not [" + expr + "]";
	}

}
