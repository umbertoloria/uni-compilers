package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

import java.util.List;

public class CallProcOP extends ExprNode {

	public final Id procId;
	public final List<ExprNode> exprs;

	public CallProcOP(Id procId, List<ExprNode> exprs) {
		this.procId = procId;
		if (procId == null) {
			throw new IllegalStateException();
		}
		this.exprs = exprs;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitCallProcOP(this);
	}

}
