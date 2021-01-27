package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class GEOP extends BinaryOperationNode {

	public GEOP(ExprNode a, ExprNode b) {
		super(">=", a, b);
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitGEOP(this);
	}

}
