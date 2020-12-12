package app.node;

import app.Node;

public class VarDeclOP extends Node {

	private TypeNode type;
	private IdInitOP idInit;

	public VarDeclOP(TypeNode type, IdInitOP idInit) {
		this.type = type;
		if (type == null) {
			throw new IllegalStateException();
		}
		this.idInit = idInit;
		if (idInit == null) {
			throw new IllegalStateException();
		}
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "VarDeclOP");
		type.visit(level + 1);
		idInit.visit(level + 1);
	}

}
