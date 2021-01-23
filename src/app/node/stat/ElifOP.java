package app.node.stat;

import app.node.ExprNode;
import app.node.StatNode;
import app.visitor.INodeVisitor;

public class ElifOP extends StatNode {

	public final ExprNode expr;
	public final BodyOP body;

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

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitElifOP(this);
	}

}
