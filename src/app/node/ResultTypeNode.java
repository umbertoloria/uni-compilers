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

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitResultTypeNode(this);
	}

	public String toString() {
		return stringType;
	}

}
