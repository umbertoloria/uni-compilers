package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class IntConst extends ExprNode {

	private int value;

	public IntConst(int value) {
		this.value = value;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitIntConst(this);
	}

	public String toString() {
		return "<IntConst, " + value + '>';
	}

}
