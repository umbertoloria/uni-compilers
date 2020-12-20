package app.node.stat;

import app.node.ExprNode;
import app.node.StatNode;
import app.visitor.INodeVisitor;

public class WhileOP extends StatNode {

	private BodyOP preStmts;
	private ExprNode expr;
	private BodyOP iterStmts;

	public WhileOP(BodyOP preStmts, ExprNode expr, BodyOP iterStmts) {
		this.preStmts = preStmts;
		this.expr = expr;
		this.iterStmts = iterStmts;
		if (iterStmts == null) {
			throw new IllegalStateException();
		}
	}

	public BodyOP getPreStmts() {
		return preStmts;
	}

	public ExprNode getExpr() {
		return expr;
	}

	public BodyOP getIterStmts() {
		return iterStmts;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitWhileOP(this);
	}

}
