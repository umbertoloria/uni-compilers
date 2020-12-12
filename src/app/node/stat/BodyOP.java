package app.node.stat;

import app.Driver;
import app.node.StatNode;

import java.util.List;

public class BodyOP extends StatNode {

	private List<StatNode> stmts;

	public BodyOP(List<StatNode> stmts) {
		this.stmts = stmts;
		if (stmts == null || stmts.isEmpty()) {
			throw new IllegalStateException();
		}
	}

	public List<StatNode> getStmts() {
		return stmts;
	}

	public void visit(int level) {
		System.out.println("    ".repeat(level) + "BodyOP");
		System.out.println("    ".repeat(level + 1) + "statements");
		Driver.visit(stmts, level + 2);
	}

}
