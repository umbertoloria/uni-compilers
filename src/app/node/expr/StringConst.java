package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class StringConst extends ExprNode {

	private String str;

	public StringConst(String str) {
		this.str = str;
	}

	public String getStr() {
		return str;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitStringConst(this);
	}

	public String toString() {
		return "<StringConst, " + str + '>';
	}

}
