package app.node.stat;

import app.node.StatNode;
import app.node.expr.CallProcOP;
import app.visitor.INodeVisitor;

public class CallProcStatOP extends StatNode {

	public final CallProcOP callProcOP;

	public CallProcStatOP(CallProcOP callProcOP) {
		this.callProcOP = callProcOP;
		if (callProcOP == null) {
			throw new IllegalStateException();
		}
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitCallProcStatOP(this);
	}

}
