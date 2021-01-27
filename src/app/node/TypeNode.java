package app.node;

import app.visitor.INodeVisitor;

public class TypeNode extends Node {

	public static final TypeNode INT = new TypeNode("int");
	public static final TypeNode FLOAT = new TypeNode("float");
	public static final TypeNode BOOL = new TypeNode("bool");
	public static final TypeNode STRING = new TypeNode("string");

	private String stringType;

	private TypeNode(String stringType) {
		this.stringType = stringType;
		if (stringType == null || stringType.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	public String getStringType() {
		return stringType;
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitTypeNode(this);
	}

	public String toString() {
		return stringType;
	}

}
