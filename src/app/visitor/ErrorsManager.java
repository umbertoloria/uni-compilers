package app.visitor;

public class ErrorsManager {

	public void typeMismatchInComparison(String typeA, String typeB) {
		System.err.println("Types '" + typeA + "' and '" + typeB + "' not suitable for comparison: only numbers or " +
				"strings or booleans can be compared");
		exit();
	}

	public void typeMismatchInUminus(String type) {
		System.err.println("Type '" + type + "' not suitable for uminus: it must be a number");
		exit();
	}

	public void typeMismatchInNegation(String type) {
		System.err.println("Type '" + type + "' not suitable for negation: it must be boolean");
		exit();
	}

	public void typeMismatchInCondition(String type) {
		System.err.println("One boolean expression is required in every conditional block: given '" + type + "'");
		exit();
	}

	public void typeMismatchInBinaryBooleanOperation(String typeA, String typeB) {
		System.err.println("Two boolean expressions are required in every binary boolean operation: given '" + typeA
				+ "' " + "and '" + typeB + "'");
		exit();
	}

	public void typeMismatchInBinaryArithmeticOperation(String typeA, String typeB) {
		System.err.println("Two number expressions are required in every binary arithmetic operation: given '" + typeA
				+ "' " + "and '" + typeB + "'");
		exit();
	}

	public void typeMismatchInAssign(String actualTypes, String formalTypes) {
		System.err.println("Invalid assignment: formal '" + formalTypes + "' but actual '" + actualTypes + "'");
		exit();
	}

	public void invalidMain() {
		System.err.println("The '" + ConstraintsVisitor.MAIN_NAME + "' procedure must return int and have no params");
		exit();
	}

	public void missingMain() {
		System.err.println("The '" + ConstraintsVisitor.MAIN_NAME + "' procedure is required");
		exit();
	}

	public void callToMain() {
		System.err.println("The '" + ConstraintsVisitor.MAIN_NAME + "' can never be called");
		exit();
	}

	public void occupiedVarName(String varName) {
		System.err.println("Variable name '" + varName + "' already taken");
		exit();
	}

	public void occupiedProcName(String procName) {
		System.err.println("Procedure name '" + procName + "' already taken");
		exit();
	}

	public void undeclaredVariable(String varName) {
		System.err.println("Undeclared variable '" + varName + "'");
		exit();
	}

	public void undeclaredProcedure(String procName) {
		System.err.println("Undeclared procedure '" + procName + "'");
		exit();
	}

	private void exit() {
		System.exit(0);
	}

}
