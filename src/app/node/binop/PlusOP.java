package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;

public class PlusOP extends BinaryOperationNode {

	public PlusOP(ExprNode a, ExprNode b) {
		super("+", a, b);
	}

}
