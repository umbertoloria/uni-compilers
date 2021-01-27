package app.node.stat;

import app.node.ExprNode;
import app.node.StatNode;
import app.visitor.INodeVisitor;

import java.util.List;

public class IfOP extends StatNode {

	public final ExprNode expr;
	public final BodyOP ifBody;
	public final List<ElifOP> elifs;
	public final BodyOP elseBody;

	public IfOP(ExprNode expr, BodyOP ifBody, List<ElifOP> elifs, BodyOP elseBody) {
		this.expr = expr;
		if (expr == null) {
			throw new IllegalStateException();
		}
		this.ifBody = ifBody;
		if (ifBody == null) {
			throw new IllegalStateException();
		}
		this.elifs = elifs;
		this.elseBody = elseBody;
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitIfOP(this);
	}

}
