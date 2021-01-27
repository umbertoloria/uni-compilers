package app.node.stat;

import app.node.StatNode;
import app.visitor.INodeVisitor;

import java.util.LinkedList;

public class BodyOP extends StatNode {

	public final LinkedList<StatNode> stmts = new LinkedList<>();

	public BodyOP(StatNode stmt) {
		prepend(stmt);
	}

	public BodyOP prepend(StatNode stmt) {
		if (stmt == null) {
			throw new IllegalStateException();
		}
		stmts.addFirst(stmt);
		return this;
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitBodyOP(this);
	}

}
