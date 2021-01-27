package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class UMinusOP extends ExprNode {

	public final ExprNode expr;

	public UMinusOP(ExprNode expr) {
		this.expr = expr;
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitUMinusOP(this);
	}

	public String toString() {
		return "-" + expr;
	}

}
