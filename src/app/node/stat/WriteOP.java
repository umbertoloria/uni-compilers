package app.node.stat;

import app.Driver;
import app.node.ExprNode;
import app.node.StatNode;
import app.visitor.INodeVisitor;

import java.util.List;

public class WriteOP extends StatNode {

	private List<ExprNode> exprs;

	public WriteOP(List<ExprNode> exprs) {
		this.exprs = exprs;
		if (exprs == null || exprs.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	public List<ExprNode> getExprs() {
		return exprs;
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "WriteOP");
		System.out.println("    ".repeat(level + 1) + "expressions");
		Driver.visit(exprs, level + 1);
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitWriteOP(this);
	}

}
