package app.node;

import app.Node;
import app.visitor.INodeVisitor;

import java.util.List;

public class VarDeclOP extends Node {

	public final TypeNode type;
	public final List<IdInitOP> idInits;

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

	@Override
	public Object accept(INodeVisitor visitor) {
		return visitor.visitVarDeclOP(this);
	}

}
