package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class FloatConst extends ExprNode {

	private float value;

	public FloatConst(float value) {
		this.value = value;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitFloatConst(this);
	}

	public String toString() {
		return "<FloatConst, " + value + '>';
	}

}
