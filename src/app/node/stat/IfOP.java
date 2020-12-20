package app.node.stat;

import app.node.ExprNode;
import app.node.StatNode;
import app.visitor.INodeVisitor;

import java.util.List;

public class IfOP extends StatNode {

	private ExprNode expr;
	private BodyOP ifBody;
	private List<ElifOP> elifs;
	private BodyOP elseBody;

	public IfOP(ExprNode expr, BodyOP ifBody, List<ElifOP> elifs, BodyOP elseBody) {
		this.expr = expr;
		this.ifBody = ifBody;
		this.elifs = elifs;
		this.elseBody = elseBody;
	}

	public ExprNode getExpr() {
		return expr;
	}

	public BodyOP getIfBody() {
		return ifBody;
	}

	public List<ElifOP> getElifs() {
		return elifs;
	}

	public BodyOP getElseBody() {
		return elseBody;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitIfOP(this);
	}

}
