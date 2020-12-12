package app.node;

import app.Node;
import app.node.expr.Id;

public class IdInitNode extends Node {

	private Id id;
	private ExprNode expr;

	public IdInitNode(Id id, ExprNode expr) {
		this.id = id;
		this.expr = expr;
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "IdInit");
		System.out.println("    ".repeat(level + 1) + " variable");
		id.visit(level + 2);
		if (expr != null) {
			System.out.println("    ".repeat(level + 1) + " assign");
			expr.visit(level + 2);
		}
	}

}
