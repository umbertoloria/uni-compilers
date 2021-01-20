package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

import java.util.List;

public class CallProcOP extends ExprNode {

	private Id procId;
	private List<ExprNode> exprs;

	public CallProcOP(Id procId, List<ExprNode> exprs) {
		this.procId = procId;
		if (procId == null) {
			throw new IllegalStateException();
		}
		this.exprs = exprs;
	}

	public Id getProcId() {
		return procId;
	}

	public List<ExprNode> getExprs() {
		return exprs;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitCallProcOP(this);
	}

}
