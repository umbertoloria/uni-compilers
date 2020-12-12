package app.node.expr;

import app.node.ExprNode;

public class UMinusOP extends ExprNode {

	private ExprNode expr;

	public UMinusOP(ExprNode expr) {
		this.expr = expr;
	}

	public String toString() {
		return "-" + expr;
	}

}
