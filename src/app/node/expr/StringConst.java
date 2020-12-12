package app.node.expr;

import app.node.ExprNode;

public class StringConst extends ExprNode {

	private String str;

	public StringConst(String str) {
		this.str = str;
	}

	public String toString() {
		return "<StringConst, " + str + '>';
	}

}
