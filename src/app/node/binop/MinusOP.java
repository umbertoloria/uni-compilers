package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;

public class MinusOP extends BinaryOperationNode {

	public MinusOP(ExprNode a, ExprNode b) {
		super("-", a, b);
	}

}
