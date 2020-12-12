package app.node.expr;

import app.node.ExprNode;

public class IntConst extends ExprNode {

	private int value;

	public IntConst(int value) {
		this.value = value;
	}

	public String toString() {
		return "<IntConst, " + value + '>';
	}

}
