package app.node.stat;

import app.node.ExprNode;
import app.node.StatNode;
import app.node.expr.Id;
import app.visitor.INodeVisitor;

import java.util.List;

public class AssignOP extends StatNode {

	private List<Id> ids;
	private List<ExprNode> exprs;

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

	public List<Id> getIds() {
		return ids;
	}

	public List<ExprNode> getExprs() {
		return exprs;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitAssignOP(this);
	}

}
