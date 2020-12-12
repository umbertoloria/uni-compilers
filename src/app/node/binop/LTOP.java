package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;

public class LTOP extends BinaryOperationNode {

	public LTOP(ExprNode a, ExprNode b) {
		super("<", a, b);
	}

}
