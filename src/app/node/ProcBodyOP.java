package app.node;

import app.Driver;
import app.Node;
import app.node.stat.BodyOP;

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

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "ProcBodyOP");
		System.out.println("    ".repeat(level + 1) + "VarDeclOP list");
		Driver.visit(varDecls, level + 1);
		System.out.println("    ".repeat(level + 1) + "Statements list");
		Driver.visit(body, level + 1);
		System.out.println("    ".repeat(level + 1) + "Return expressions list");
		Driver.visit(returnExpressions, level + 1);
	}

}
