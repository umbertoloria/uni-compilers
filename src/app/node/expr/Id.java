package app.node.expr;

import app.node.ExprNode;
import app.visitor.INodeVisitor;

public class Id extends ExprNode {

	private String nome;

	public Id(String nome) {
		this.nome = nome;
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitId(this);
	}

	public String toString() {
		return "<ID, " + nome + ">";
	}

}
