package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class StringConst extends ExprNode {

	public final String str;

	public StringConst(String str) {
		this.str = str;
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitStringConst(this);
	}

	public String toString() {
		return "<StringConst, " + str + '>';
	}

}
