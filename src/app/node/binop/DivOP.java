package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;

public class DivOP extends BinaryOperationNode {

	public DivOP(ExprNode a, ExprNode b) {
		super("/", a, b);
	}

}
