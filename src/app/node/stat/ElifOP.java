package app.node.stat;

import app.node.ExprNode;
import app.node.StatNode;

public class ElifOP extends StatNode {

	private ExprNode expr;
	private BodyOP body;

	public ElifOP(ExprNode expr, BodyOP body) {
		this.expr = expr;
		this.body = body;
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "ElifOP");
		System.out.println("    ".repeat(level + 1) + "condition");
		expr.visit(level + 2);
		body.visit(level + 1);
	}

}
