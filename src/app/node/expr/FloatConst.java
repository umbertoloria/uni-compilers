package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class FloatConst extends ExprNode {

	public final float value;

	public FloatConst(float value) {
		this.value = value;
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitFloatConst(this);
	}

	public String toString() {
		return "<FloatConst, " + value + '>';
	}

}
