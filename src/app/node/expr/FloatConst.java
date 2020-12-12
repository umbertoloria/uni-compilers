package app.node.expr;

import app.node.ExprNode;

public class FloatConst extends ExprNode {

	private float value;

	public FloatConst(float value) {
		this.value = value;
	}

	public String toString() {
		return "<FloatConst, " + value + '>';
	}

}
