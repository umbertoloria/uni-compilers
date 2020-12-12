package app;

import app.visitor.INodeVisitor;

public abstract class Node {

	public abstract void visit(int level);

	public abstract Object accept(INodeVisitor visitor);

}
