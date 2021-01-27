package app.node.stat;

import app.node.StatNode;
import app.node.expr.Id;
import app.visitor.INodeVisitor;

import java.util.List;

public class ReadlnOP extends StatNode {

	public final List<Id> ids;

	public ReadlnOP(List<Id> ids) {
		this.ids = ids;
		if (ids == null || ids.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitReadlnOP(this);
	}

}
