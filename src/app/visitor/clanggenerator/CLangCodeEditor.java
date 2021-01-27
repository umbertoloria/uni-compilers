package app.visitor.clanggenerator;

import app.node.TypeNode;

import java.util.*;

public class CLangCodeEditor {

	public void putCLikeExpr(String line) {
		code.append(line);
		code.append("\n");
	}

	public String getCode() {
		return code.toString();
	}

	private String getTabbedCode() {
		StringBuilder tabbed = new StringBuilder();
		for (String line : code.toString().split("\n")) {
			tabbed.append("    ");
			tabbed.append(line);
			tabbed.append("\n");
		}
		stack.pop();
		code = stack.peek();
		return tabbed.toString();
	}

	// BL
	private Stack<StringBuilder> stack = new Stack<>();
	private StringBuilder code = new StringBuilder();
	private Set<String> declaredStructs = new HashSet<>();

	public CLangCodeEditor() {
		stack.push(code);
	}

	public void importLibraries() {
		code.append("#include <stdio.h>\n");
		code.append("#include <string.h>\n\n");
	}

	public void putInterface(String cRetType, String name, List<String> paramTypes) {
		// Es. 'int_int_int getCurDate(int zeroPaddingDay, int zeroPaddingMonth);'
		code.append(cRetType).append(" ").append(name).append("(");
		if (!paramTypes.isEmpty()) {
			for (String paramType : paramTypes) {
				code.append(paramType);
				code.append(", ");
			}
			code.delete(code.length() - 2, code.length());
		}
		code.append(");\n");
	}

	public void putVarDeclaration(String cType, String varName) {
		// Es. 'int flag = 0;'
		code.append(cType).append(" ").append(varName).append(" = ")
				.append(TypeCodifier.defaultValueForTypes.get(cType)).append(";\n");
	}

	public void putVarInitialization(String cType, String varName, String cExpr) {
		// Es. 'int var = 2*n+3;'
		code.append(cType).append(" ").append(varName).append(" = ").append(cExpr).append(";\n");
	}

	public void putMultipleReturn(String retStructName, List<String> cExprs) {
		if (retStructName.split("_").length != cExprs.size()) {
			throw new IllegalStateException();
		}
		// Es. 'int_int res;'
		//     'res.t0 = 3*7;'
		//     'res.t1 = min(a, 1);'
		//     'return res;'
		code.append(retStructName).append(" res;\n");
		int i = 0;
		for (String retExpr : cExprs) {
			code.append("res.t").append(i++).append(" = ").append(retExpr).append(";\n");
		}
		code.append("return res;\n");
	}

	public void putReturn(String cExpr) {
		// Es. 'return -min(a, b);'
		code.append("return ").append(cExpr).append(";\n");
	}

	// BLOCKS
	public void openBlock() {
		StringBuilder newCode = new StringBuilder();
		stack.push(newCode);
		code = stack.peek();
	}

	public void createStruct(String name, List<String> cTypes) {
		if (!declaredStructs.contains(name)) {
			declaredStructs.add(name);
			// Es. 'typedef struct int_string {'
			//     '    int t0;'
			//     '    char* t1;'
			//     '} int_string;'
			code.append("typedef struct ").append(name).append(" {\n"); // MOVE THIS
			openBlock();
			int varI = 0;
			for (String cType : cTypes) {
				code.append(cType).append(" t").append(varI++).append(";\n");
			}
			String tabbed = getTabbedCode();
			code.append(tabbed);
			code.append("} ").append(name).append(";\n");
		}
	}

	public void scanf(String name, String type) {
		code.append("scanf(\"");
		if (type.equals(TypeNode.INT.getStringType()))
			code.append("%d\", &");
		else if (type.equals(TypeNode.FLOAT.getStringType()))
			code.append("%f\", &");
		else if (type.equals(TypeNode.BOOL.getStringType()))
			// TODO: check only '0' or '1'
			code.append("%d\", &");
		else if (type.equals(TypeNode.STRING.getStringType()))
			code.append("%s\", ");
		code.append(name).append(");\n");
	}

	public void printf(String cExpr, String type) {
		if (type.equals(TypeNode.STRING.getStringType())) {
			code.append("printf(").append(cExpr).append(");\n");
		} else {
			code.append("printf(\"");
			if (type.equals(TypeNode.INT.getStringType()))
				code.append("%d");
			else if (type.equals(TypeNode.FLOAT.getStringType()))
				code.append("%f");
			else if (type.equals(TypeNode.BOOL.getStringType()))
				// TODO: check only '0' or '1'
				code.append("%d");
			code.append("\", ").append(cExpr).append(");\n");
		}
	}

	public void assign(String varName, String cExpr) {
		code.append(varName).append(" = ").append(cExpr).append(";\n");
	}

	public void invoke(String procName, List<String> cExprs) {
		code.append(procName);
		code.append("(");
		if (cExprs != null && !cExprs.isEmpty()) {
			for (String cExpr : cExprs) {
				code.append(cExpr);
				code.append(", ");
			}
			code.delete(code.length() - 2, code.length());
		}
		code.append(");\n");
	}

	public void invokeWithResult(String varType, String varName, String procName, List<String> cExprs) {
		code.append(varType);
		code.append(" ");
		code.append(varName);
		code.append(" = ");
		invoke(procName, cExprs);
	}

	public void openIfBlock(String cExpr) {
		code.append("if (").append(cExpr).append(") {\n");
		openBlock();
	}

	public void openElseIfBlock(String cExpr) {
		code.append("else if (").append(cExpr).append(") {\n");
		openBlock();
	}

	public void openElseBlock() {
		code.append("else {\n");
		openBlock();
	}

	public void openWhileBlock(String cExpr) {
		code.append("while (").append(cExpr).append(") {\n");
		openBlock();
	}

	public void closeBlock() {
		String tabbed = getTabbedCode();
		code.append(tabbed);
		code.append("}\n");
	}

	public void openDeclarationBlock(String cRetType, String name, List<String> cParamTypes, List<String> paramNames) {
		if (cParamTypes.size() != paramNames.size())
			throw new IllegalStateException();
		code.append(cRetType).append(" ").append(name).append("(");
		if (!paramNames.isEmpty()) {
			Iterator<String> it1 = cParamTypes.iterator();
			Iterator<String> it2 = paramNames.iterator();
			while (it1.hasNext()) {
				String paramType = it1.next();
				String paramName = it2.next();
				code.append(paramType);
				code.append(" ");
				code.append(paramName);
				code.append(", ");
			}
			code.delete(code.length() - 2, code.length());
		}
		code.append(")\n{\n");
		openBlock();
	}

}
