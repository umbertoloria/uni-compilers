package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;

public class GTOP extends BinaryOperationNode {

	public GTOP(ExprNode a, ExprNode b) {
		super(">", a, b);
	}

}
