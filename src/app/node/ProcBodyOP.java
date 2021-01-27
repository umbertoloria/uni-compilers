package app.node;

import app.node.stat.BodyOP;
import app.visitor.INodeVisitor;

import java.util.List;

public class ProcBodyOP extends Node {

	public final List<VarDeclOP> varDecls;
	public final BodyOP body;
	public final List<ExprNode> returnExpressions;

	public ProcBodyOP(List<VarDeclOP> varDecls, BodyOP body, List<ExprNode> returnExpressions) {
		this.varDecls = varDecls;
		this.body = body;
		this.returnExpressions = returnExpressions;
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitProcBodyOP(this);
	}

}
