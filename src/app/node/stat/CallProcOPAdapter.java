package app.node.stat;

import app.node.StatNode;
import app.node.expr.CallProcOP;
import app.visitor.INodeVisitor;

public class CallProcOPAdapter extends StatNode {

	private CallProcOP callProcOP;

	public CallProcOPAdapter(CallProcOP callProcOP) {
		this.callProcOP = callProcOP;
		if (callProcOP == null) {
			throw new IllegalStateException();
		}
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return callProcOP.accept(visitor);
	}

}
