package app.node;

import app.Node;
import app.visitor.INodeVisitor;

public class ResultTypeNode extends Node {

	private String stringType;

	public ResultTypeNode(TypeNode type) {
		this.stringType = type.getStringType();
	}

	public ResultTypeNode() {
		this.stringType = "void";
	}

	public String getStringType() {
		return stringType;
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "ResultType");
		System.out.println("    ".repeat(level + 1) + stringType);
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitResultTypeNode(this);
	}

	public String toString() {
		return stringType;
	}

}
