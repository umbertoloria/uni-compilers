package app.node;

import app.Node;

public class ExprNode extends Node {

	public void visit(int level) {
		System.out.println("    ".repeat(level) + this);
	}

}
