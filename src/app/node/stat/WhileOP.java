package app.node.stat;

import app.node.ExprNode;
import app.node.StatNode;

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

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "WhileOP");
		if (preStmts != null) {
			System.out.println("    ".repeat(level + 1) + "prior statements");
			preStmts.visit(level + 1);
		}
		System.out.println("    ".repeat(level + 1) + "condition");
		expr.visit(level + 2);
		System.out.println("    ".repeat(level + 1) + "iterating statements");
		iterStmts.visit(level + 1);
	}

}
