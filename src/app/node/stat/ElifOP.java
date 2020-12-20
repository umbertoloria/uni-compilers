package app.node.stat;

import app.node.ExprNode;
import app.node.StatNode;
import app.visitor.INodeVisitor;

public class ElifOP extends StatNode {

	private ExprNode expr;
	private BodyOP body;

	public ElifOP(ExprNode expr, BodyOP body) {
		this.expr = expr;
		if (expr == null) {
			throw new IllegalStateException();
		}
		this.body = body;
		if (body == null) {
			throw new IllegalStateException();
		}
	}

	public ExprNode getExpr() {
		return expr;
	}

	public BodyOP getBody() {
		return body;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitElifOP(this);
	}

}
