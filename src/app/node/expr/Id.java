package app.node.expr;

import app.node.ExprNode;

public class Id extends ExprNode {

	private String nome;

	public Id(String nome) {
		this.nome = nome;
	}

	public String toString() {
		return "<ID, " + nome + ">";
	}

}
