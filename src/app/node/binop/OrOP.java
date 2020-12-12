package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;

public class OrOP extends BinaryOperationNode {

	public OrOP(ExprNode a, ExprNode b) {
		super("||", a, b);
	}

}
