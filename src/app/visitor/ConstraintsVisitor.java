package app.visitor;

import app.node.ProgramOP;
import app.node.expr.CallProcOP;
import app.visitor.scoping.ScopingTable;

public class ConstraintsVisitor extends DFSBaseVisitor<Void> {

	public static final String MAIN_NAME = "main";

	private ScopingTable scopingTable;
	private ErrorsManager errorsManager = new ErrorsManager();

	public ConstraintsVisitor(ScopingTable rootScopingTable) {
		this.scopingTable = rootScopingTable;
	}

	@Override
	public Void visitProgramOP(ProgramOP programOP) {
		try {
			if (!scopingTable.getTypeInCloserScopeGoingUp(MAIN_NAME).equals("->int")) {
				errorsManager.invalidMain();
				return null;
			}
		} catch (Exception e) {
			errorsManager.missingMain();
			return null;
		}
		return super.visitProgramOP(programOP);
	}

	@Override
	public Void visitCallProcOP(CallProcOP callProcOP) {
		if (callProcOP.procId.name.equals(MAIN_NAME)) {
			errorsManager.callToMain();
			return null;
		}
		return super.visitCallProcOP(callProcOP);
	}

}
