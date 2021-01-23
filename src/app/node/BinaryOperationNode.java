package app.node;

public abstract class BinaryOperationNode extends ExprNode {

	public final String op;
	public final ExprNode a, b;

	public BinaryOperationNode(String op, ExprNode a, ExprNode b) {
		this.a = a;
		this.b = b;
		this.op = op;
	}

	public String toString() {
		return a + " " + op + " " + b;
	}

}
