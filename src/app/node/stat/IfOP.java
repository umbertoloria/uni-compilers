package app.node.stat;

import app.Driver;
import app.node.ExprNode;
import app.node.StatNode;

import java.util.List;

public class IfOP extends StatNode {

	private ExprNode expr;
	private BodyOP ifBody;
	private List<ElifOP> elifs;
	private BodyOP elseBody;

	public IfOP(ExprNode expr, BodyOP ifBody, List<ElifOP> elifs, BodyOP elseBody) {
		this.expr = expr;
		this.ifBody = ifBody;
		this.elifs = elifs;
		this.elseBody = elseBody;
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "IfStatOP");
		System.out.println("    ".repeat(level + 1) + "condition");
		expr.visit(level + 2);
		System.out.println("    ".repeat(level + 1) + "If body");
		ifBody.visit(level + 1);
		if (elifs != null) {
			System.out.println("    ".repeat(level + 1) + "ElifOP list");
			Driver.visit(elifs, level + 1);
		}
		if (elseBody != null) {
			System.out.println("    ".repeat(level + 1) + "Else body");
			elseBody.visit(level + 1);
		}
	}

}
