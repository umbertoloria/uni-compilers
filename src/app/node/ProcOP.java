package app.node;

import app.Driver;
import app.Node;
import app.node.expr.Id;
import app.visitor.INodeVisitor;

import java.util.List;

public class ProcOP extends Node {

	private Id id;
	private List<ParDeclOP> parDecls;
	private List<ResultTypeNode> returnTypes;
	private ProcBodyOP procBody;

	public ProcOP(Id id, List<ParDeclOP> parDecls, List<ResultTypeNode> returnTypes, ProcBodyOP procBody) {
		this.id = id;
		this.parDecls = parDecls;
		this.returnTypes = returnTypes;
		this.procBody = procBody;
		if (returnTypes == null || returnTypes.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "ProcOP := " + id);
		System.out.println("    ".repeat(level + 1) + "ParDeclOP list");
		Driver.visit(parDecls, level + 1);
		System.out.println("    ".repeat(level + 1) + "Return types");
		Driver.visit(returnTypes, level + 1);
		procBody.visit(level + 1);
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitProcOP(this);
	}

}
