package app.node.stat;

import app.node.ExprNode;
import app.node.StatNode;
import app.node.expr.Id;
import app.visitor.INodeVisitor;

import java.util.List;

public class AssignOP extends StatNode {

	public final List<Id> ids;
	public final List<ExprNode> exprs;

	public AssignOP(List<Id> ids, List<ExprNode> exprs) {
		this.ids = ids;
		if (ids == null || ids.isEmpty()) {
			throw new IllegalStateException();
		}
		this.exprs = exprs;
		if (exprs == null || exprs.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitAssignOP(this);
	}

}
