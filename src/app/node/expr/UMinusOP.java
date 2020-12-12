package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class UMinusOP extends ExprNode {

	private ExprNode expr;

	public UMinusOP(ExprNode expr) {
		this.expr = expr;
	}

	public ExprNode getExpr() {
		return expr;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitUMinusOP(this);
	}

	public String toString() {
		return "-" + expr;
	}

}
