package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class True extends ExprNode {

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitTrue(this);
	}

	public String toString() {
		return "<True>";
	}

}
