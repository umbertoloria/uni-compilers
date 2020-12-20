package app.node;

public abstract class BinaryOperationNode extends ExprNode {

	private String op;
	private ExprNode a, b;

	public BinaryOperationNode(String op, ExprNode a, ExprNode b) {
		this.a = a;
		this.b = b;
		this.op = op;
	}

	public String getOp() {
		return op;
	}

	public ExprNode getA() {
		return a;
	}

	public ExprNode getB() {
		return b;
	}

	public String toString() {
		return a + " " + op + " " + b;
	}

}
