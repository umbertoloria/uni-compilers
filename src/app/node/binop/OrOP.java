package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class OrOP extends BinaryOperationNode {

	public OrOP(ExprNode a, ExprNode b) {
		super("||", a, b);
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitOrOP(this);
	}

}
