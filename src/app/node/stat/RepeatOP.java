package app.node.stat;

import app.node.ExprNode;
import app.node.StatNode;
import app.node.VarDeclOP;
import app.visitor.INodeVisitor;

import java.util.List;

public class RepeatOP extends StatNode {

	public final String name;
	public final List<VarDeclOP> varDecls;
	public final BodyOP stmts;
	public final ExprNode expr;

	public RepeatOP(String name, List<VarDeclOP> varDecls, BodyOP stmts, ExprNode expr) {
		this.name = name;
		if (name == null) {
			throw new IllegalStateException();
		}
		this.varDecls = varDecls;
		if (varDecls == null) {
			throw new IllegalStateException();
		}
		this.stmts = stmts;
		if (stmts == null) {
			throw new IllegalStateException();
		}
		this.expr = expr;
		if (expr == null) {
			throw new IllegalStateException();
		}
	}

	@Override
	public <T> T accept(INodeVisitor<T> visitor) {
		return visitor.visitRepeatOP(this);
	}

}
