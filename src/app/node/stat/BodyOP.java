package app.node.stat;

import app.Driver;
import app.node.StatNode;
import app.visitor.INodeVisitor;

import java.util.LinkedList;

public class BodyOP extends StatNode {

	private LinkedList<StatNode> stmts = new LinkedList<>();

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

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "BodyOP");
		System.out.println("    ".repeat(level + 1) + "statements");
		Driver.visit(stmts, level + 2);
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitBodyOP(this);
	}

}
