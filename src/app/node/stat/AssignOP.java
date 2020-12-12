package app.node.stat;

import app.Driver;
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
		this.exprs = exprs;
		if (ids == null || ids.isEmpty()) {
			throw new IllegalStateException();
		}
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

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "AssignOP");
		System.out.println("    ".repeat(level + 1) + "from " + ids.toString());
		System.out.println("    ".repeat(level + 1) + "to");
		Driver.visit(exprs, level + 2);
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitAssignOP(this);
	}

}
