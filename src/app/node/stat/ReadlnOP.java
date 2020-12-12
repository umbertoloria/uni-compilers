package app.node.stat;

import app.node.StatNode;
import app.node.expr.Id;
import app.visitor.INodeVisitor;

import java.util.List;

public class ReadlnOP extends StatNode {

	private List<Id> ids;

	public ReadlnOP(List<Id> ids) {
		this.ids = ids;
		if (ids == null || ids.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "ReadlnOP " + ids.toString());
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitReadlnOP(this);
	}

}
