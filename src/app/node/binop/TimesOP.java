package app.node.binop;

import app.node.BinaryOperationNode;
import app.node.ExprNode;

public class TimesOP extends BinaryOperationNode {

	public TimesOP(ExprNode a, ExprNode b) {
		super("*", a, b);
	}

}
