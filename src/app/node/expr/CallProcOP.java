package app.node.expr;

import app.Driver;
import app.node.ExprNode;
import app.visitor.INodeVisitor;

import java.util.List;

public class CallProcOP extends ExprNode {

	private Id procId;
	private List<ExprNode> exprs;

	public CallProcOP(Id procId, List<ExprNode> exprs) {
		this.procId = procId;
		this.exprs = exprs;
	}

	public Id getProcId() {
		return procId;
	}

	public List<ExprNode> getExprs() {
		return exprs;
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "CallProcOP");
		System.out.println("    ".repeat(level + 1) + "Procedure Id: " + procId);
		System.out.println("    ".repeat(level + 1) + "Expressions");
		Driver.visit(exprs, level + 2);
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitCallProcOP(this);
	}

}
