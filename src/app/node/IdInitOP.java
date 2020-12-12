package app.node;

import app.Node;
import app.node.expr.Id;
import app.visitor.INodeVisitor;

public class IdInitOP extends Node {

	private Id id;
	private ExprNode expr;

	public IdInitOP(Id id, ExprNode expr) {
		this.id = id;
		this.expr = expr;
	}

	public Id getId() {
		return id;
	}

	public ExprNode getExpr() {
		return expr;
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

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitIdInitOP(this);
	}

}
