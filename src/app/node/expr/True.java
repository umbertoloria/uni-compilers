package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class True extends ExprNode {

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitTrue(this);
	}

	public String toString() {
		return "<True>";
	}

}
