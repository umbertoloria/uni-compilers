package app.node;

import app.Driver;
import app.Node;
import app.visitor.INodeVisitor;

import java.util.List;

public class VarDeclOP extends Node {

	private TypeNode type;
	private List<IdInitOP> idInits;

	public VarDeclOP(TypeNode type, List<IdInitOP> idInits) {
		this.type = type;
		if (type == null) {
			throw new IllegalStateException();
		}
		this.idInits = idInits;
		if (idInits == null || idInits.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	public TypeNode getType() {
		return type;
	}

	public List<IdInitOP> getIdInits() {
		return idInits;
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "VarDeclOP");
		type.visit(level + 1);
		System.out.println("    ".repeat(level + 1) + "IdInitOP list");
		Driver.visit(idInits, level + 1);
	}

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitVarDeclOP(this);
	}

}
