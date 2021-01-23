package app.node.stat;

import app.node.ExprNode;
import app.node.StatNode;
import app.visitor.INodeVisitor;

public class WhileOP extends StatNode {

	public final BodyOP preStmts;
	public final ExprNode expr;
	public final BodyOP iterStmts;

	public WhileOP(BodyOP preStmts, ExprNode expr, BodyOP iterStmts) {
		this.preStmts = preStmts;
		this.expr = expr;
		if (expr == null) {
			throw new IllegalStateException();
		}
		this.iterStmts = iterStmts;
		if (iterStmts == null) {
			throw new IllegalStateException();
		}
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitWhileOP(this);
	}

}
