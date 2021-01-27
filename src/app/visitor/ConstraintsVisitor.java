package app.visitor;

import app.node.ProgramOP;
import app.node.expr.CallProcOP;
import app.visitor.scoping.ScopingTable;

public class ConstraintsVisitor extends DFSBaseVisitor<Void> {

	public static final String MAIN_PROC_NAME = "main";

	private ScopingTable scopingTable;

	public ConstraintsVisitor(ScopingTable rootScopingTable) {
		this.scopingTable = rootScopingTable;
	}

	@Override
	public Void visitProgramOP(ProgramOP programOP) {
		try {
			if (!scopingTable.getTypeInCloserScopeGoingUp(MAIN_PROC_NAME).equals("->int")) {
				throw new IllegalStateException("The '" + MAIN_PROC_NAME + "' procedure must return int and have no " +
						"params");
			}
		} catch (IllegalStateException e) {
			throw new IllegalStateException("The '" + MAIN_PROC_NAME + "' procedure is required");
		}
		return super.visitProgramOP(programOP);
	}

	@Override
	public Void visitCallProcOP(CallProcOP callProcOP) {
		if (callProcOP.procId.name.equals(MAIN_PROC_NAME)) {
			throw new IllegalStateException("The '" + MAIN_PROC_NAME + "' can never be called");
		}
		return super.visitCallProcOP(callProcOP);
	}

}
