package app.visitor;

public class ErrorsManager {

	public void typeMismatchInComparison(String typeA, String typeB) {
		throw new RuntimeException("Types '" + typeA + "' and '" + typeB + "' not suitable for comparison: only "
				+ "numbers or strings or booleans can be compared");
	}

	public void typeMismatchInUminus(String type) {
		throw new RuntimeException("Type '" + type + "' not suitable for uminus: it must be a number");
	}

	public void typeMismatchInNegation(String type) {
		throw new RuntimeException("Type '" + type + "' not suitable for negation: it must be boolean");
	}

	public void typeMismatchInCondition(String type) {
		throw new RuntimeException("One boolean expression is required in every conditional block: given '" + type
				+ "'");
	}

	public void typeMismatchInBinaryBooleanOperation(String typeA, String typeB) {
		throw new RuntimeException("Two boolean expressions are required in every binary boolean operation: given '"
				+ typeA + "' " + "and '" + typeB + "'");
	}

	public void typeMismatchInConcatenationOperation(String typeA, String typeB) {
		throw new RuntimeException("Two string expressions are required for concatenation: given '" + typeA
				+ "' and '" + typeB + "'");
	}

	public void typeMismatchInBinaryArithmeticOperation(String typeA, String typeB) {
		throw new RuntimeException("Two number expressions are required in every binary arithmetic operation: given '"
				+ typeA + "' and '" + typeB + "'");
	}

	public void typeMismatchInAssign(String actualTypes, String formalTypes) {
		throw new RuntimeException("Invalid assignment: formal '" + formalTypes + "' but actual '" + actualTypes
				+ "'");
	}

	public void invalidMain() {
		throw new RuntimeException("The '" + ConstraintsVisitor.MAIN_NAME + "' procedure must return int and have no "
				+ "params");
	}

	public void missingMain() {
		throw new RuntimeException("The '" + ConstraintsVisitor.MAIN_NAME + "' procedure is required");
	}

	public void callToMain() {
		throw new RuntimeException("The '" + ConstraintsVisitor.MAIN_NAME + "' can never be called");
	}

	public void occupiedVarName(String varName) {
		throw new RuntimeException("Variable name '" + varName + "' already taken");
	}

	public void occupiedProcName(String procName) {
		throw new RuntimeException("Procedure name '" + procName + "' already taken");
	}

	public void undeclaredVariable(String varName) {
		throw new RuntimeException("Undeclared variable '" + varName + "'");
	}

	public void undeclaredProcedure(String procName) {
		throw new RuntimeException("Undeclared procedure '" + procName + "'");
	}

}
