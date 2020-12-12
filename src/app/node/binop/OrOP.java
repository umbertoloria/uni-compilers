package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class OrOP extends BinaryOperationNode {

	public OrOP(ExprNode a, ExprNode b) {
		super("||", a, b);
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitOrOP(this);
	}

}
