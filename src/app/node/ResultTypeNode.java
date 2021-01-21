package app.node;

import app.Node;
import app.visitor.INodeVisitor;

public class ResultTypeNode extends Node {

	public static final ResultTypeNode VOID = new ResultTypeNode();

	private String stringType;

	public ResultTypeNode(TypeNode type) {
		if (type == null) {
			throw new IllegalStateException();
		}
		this.stringType = type.getStringType();
	}

	private ResultTypeNode() {
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
