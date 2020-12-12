package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;

public class LEOP extends BinaryOperationNode {

	public LEOP(ExprNode a, ExprNode b) {
		super("<=", a, b);
	}

}
