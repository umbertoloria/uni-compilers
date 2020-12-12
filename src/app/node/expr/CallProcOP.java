package app.node.expr;

import app.Driver;
import app.node.ExprNode;

import java.util.List;

public class CallProcOP extends ExprNode {

	private Id procId;
	private List<ExprNode> exprs;

	public CallProcOP(Id procId, List<ExprNode> exprs) {
		this.procId = procId;
		this.exprs = exprs;
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "CallProcOP");
		System.out.println("    ".repeat(level + 1) + "Procedure Id: " + procId);
		System.out.println("    ".repeat(level + 1) + "Expressions");
		Driver.visit(exprs, level + 2);
	}

}
