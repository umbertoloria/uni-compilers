package app.node;

import app.Node;

public class TypeNode extends Node {

	public static final TypeNode INT = new TypeNode("int");
	public static final TypeNode FLOAT = new TypeNode("float");
	public static final TypeNode BOOL = new TypeNode("bool");
	public static final TypeNode STRING = new TypeNode("string");

	private String type;

	private TypeNode(String type) {
		this.type = type;
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "Type");
		System.out.println("    ".repeat(level + 1) + type);
	}

	public String toString() {
		return type;
	}

}
