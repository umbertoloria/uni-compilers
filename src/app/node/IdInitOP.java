package app.node;

import app.Driver;
import app.Node;

import java.util.List;

public class IdInitOP extends Node {

	private List<IdInitNode> idInits;

	public IdInitOP(List<IdInitNode> idInits) {
		this.idInits = idInits;
		if (idInits == null) {
			throw new IllegalStateException();
		}
	}

	public List<IdInitNode> getIdInits() {
		return idInits;
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "IdInitOP");
		System.out.println("    ".repeat(level + 1) + "IdInit list");
		Driver.visit(idInits, level + 1);
	}

}
