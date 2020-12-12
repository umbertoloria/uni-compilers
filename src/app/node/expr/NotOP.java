package app.node.expr;

import app.node.ExprNode;

public class NotOP extends ExprNode {

	private ExprNode expr;

	public NotOP(ExprNode expr) {
		this.expr = expr;
	}

	public String toString() {
		return "Not [" + expr + "]";
	}

}
