package app;

import app.visitor.INodeVisitor;

public abstract class Node {

	public abstract Object accept(INodeVisitor visitor);

}
