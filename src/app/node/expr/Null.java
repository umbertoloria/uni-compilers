package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class Null extends ExprNode {

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitNull(this);
	}

	public String toString() {
		return "<Null>";
	}

}
