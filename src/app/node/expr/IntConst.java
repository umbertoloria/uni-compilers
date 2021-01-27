package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class IntConst extends ExprNode {

	public final int value;

	public IntConst(int value) {
		this.value = value;
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitIntConst(this);
	}

	public String toString() {
		return "<IntConst, " + value + '>';
	}

}
