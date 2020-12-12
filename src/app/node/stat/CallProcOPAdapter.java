package app.node.stat;

import app.node.StatNode;
import app.node.expr.CallProcOP;
import app.visitor.INodeVisitor;

public class CallProcOPAdapter extends StatNode {

	private CallProcOP callProcOP;

	public CallProcOPAdapter(CallProcOP callProcOP) {
		this.callProcOP = callProcOP;
	}

	public void visit(int level) {
		callProcOP.visit(level);
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return callProcOP.accept(visitor);
	}

}
