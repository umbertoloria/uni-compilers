package app.node;

import app.Node;
import app.node.stat.BodyOP;
import app.visitor.INodeVisitor;

import java.util.List;

public class ProcBodyOP extends Node {

	private List<VarDeclOP> varDecls;
	private BodyOP body;
	private List<ExprNode> returnExpressions;

	public ProcBodyOP(List<VarDeclOP> varDecls, BodyOP body, List<ExprNode> returnExpressions) {
		this.varDecls = varDecls;
		this.body = body;
		this.returnExpressions = returnExpressions;
	}

	public List<VarDeclOP> getVarDecls() {
		return varDecls;
	}

	public BodyOP getBody() {
		return body;
	}

	public List<ExprNode> getReturnExpressions() {
		return returnExpressions;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitProcBodyOP(this);
	}

}
