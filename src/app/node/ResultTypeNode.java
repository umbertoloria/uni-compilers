package app.node;

import app.Node;

public class ResultTypeNode extends Node {

	private TypeNode type;

	public ResultTypeNode(TypeNode type) {
		this.type = type;
	}

	public ResultTypeNode() {
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "ResultType");
		if (type != null) {
			type.visit(level + 1);
		} else {
			System.out.println("    ".repeat(level + 1) + "void");
		}
	}

	public String toString() {
		return type != null ? type.toString() : "void";
	}

}
