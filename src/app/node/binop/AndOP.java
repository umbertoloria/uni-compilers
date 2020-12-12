package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;

public class AndOP extends BinaryOperationNode {

	public AndOP(ExprNode a, ExprNode b) {
		super("&&", a, b);
	}

}
