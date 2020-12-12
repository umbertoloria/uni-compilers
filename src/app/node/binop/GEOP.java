package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;

public class GEOP extends BinaryOperationNode {

	public GEOP(ExprNode a, ExprNode b) {
		super(">=", a, b);
	}

}
