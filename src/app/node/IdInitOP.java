package app.node;

import app.Node;
import app.node.expr.Id;
import app.visitor.INodeVisitor;

public class IdInitOP extends Node {

	public final Id id;
	public final ExprNode expr;

	public IdInitOP(Id id, ExprNode expr) {
		this.id = id;
		if (id == null) {
			throw new IllegalStateException();
		}
		this.expr = expr;
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitIdInitOP(this);
	}

}
