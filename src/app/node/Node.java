package app.node;

import app.visitor.INodeVisitor;

public abstract class Node {

	public abstract <T> T accept(INodeVisitor<T> visitor);

}
