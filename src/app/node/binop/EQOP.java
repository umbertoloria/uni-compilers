package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;

public class EQOP extends BinaryOperationNode {

	public EQOP(ExprNode a, ExprNode b) {
		super("==", a, b);
	}

}
