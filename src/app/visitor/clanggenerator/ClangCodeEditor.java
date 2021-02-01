package app.visitor.clanggenerator;

import app.node.TypeNode;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class ClangCodeEditor {

	private static final String tab = "\t";

	private EasyNamesManager easyNamesManager;
	private Stack<StringBuilder> stack = new Stack<>();
	private StringBuilder code;

	public ClangCodeEditor(EasyNamesManager easyNamesManager) {
		this.easyNamesManager = easyNamesManager;
		code = new StringBuilder();
		code.append("#include <stdio.h>\n");
		code.append("#include <string.h>\n");
		stack.push(code);
	}

	public void closeBlock() {
		int nlPosition = -1;
		do {
			code.insert(nlPosition + 1, tab);
			nlPosition = code.indexOf("\n", nlPosition + 1);
		} while (nlPosition >= 0);
		int lastNlPosition = code.lastIndexOf("\n");
		if (lastNlPosition >= 0 && lastNlPosition == code.length() - tab.length() - 1) {
			code.delete(lastNlPosition + 1, code.length());
		}
		stack.pop();
		stack.peek().append(code);
		code = stack.peek();
		code.append("}\n");
	}

	public void putInterface(String cRetType, String procName, List<String> paramTypes) {
		// Es. 'int_int_int getCurDate(int zeroPaddingDay, int zeroPaddingMonth);'
		code.append(cRetType).append(" ").append(procName).append("(");
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

	public void putCLikeExpr(String line) {
		code.append(line);
		code.append("\n");
	}

	public void putSingleReturn(String cExpr) {
		// Es. 'return -min(a, b);'
		code.append("return ").append(cExpr).append(";\n");
	}

	public void putMultipleReturn(String structName, List<String> cExprs) {
		// Es. 'int_int res;'
		//     'res.t0 = 3*7;'
		//     'res.t1 = min(a, 1);'
		//     'return res;'
		code.append(structName).append(" res;\n");
		int i = 0;
		for (String retExpr : cExprs) {
			code.append("res.t").append(i++).append(" = ").append(retExpr).append(";\n");
		}
		code.append("return res;\n");
	}

	public void putStruct(String structName, List<String> cTypes) {
		// Es. 'typedef struct {'
		//     '    int t0;'
		//     '    char* t1;'
		//     '} int_string;'
		code.append("typedef struct {\n");
		stack.push(code = new StringBuilder());
		int varI = 0;
		for (String cType : cTypes) {
			code.append(cType).append(" t").append(varI++).append(";\n");
		}
		closeBlock();
		code.delete(code.length() - 1, code.length()).append(" ").append(structName).append(";\n");
	}

	public void scanf(String name, String type) {
		if (type.equals(TypeNode.STRING.getStringType())) {
			String tmpVarName = easyNamesManager.createName("in_str");
			code.append("char ").append(tmpVarName).append("[512];\n");
			code.append("scanf(\"%s\", ").append(tmpVarName).append(");\n");
			code.append(name).append(" = ").append(tmpVarName).append(";\n");
		} else {
			code.append("scanf(\"");
			if (type.equals(TypeNode.INT.getStringType()) || type.equals(TypeNode.BOOL.getStringType()))
				code.append("%d");
			else if (type.equals(TypeNode.FLOAT.getStringType()))
				code.append("%f");
			code.append("\", &").append(name).append(");\n");
		}
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
				code.append("%d");
			code.append("\", ").append(cExpr).append(");\n");
		}
	}

	public void assign(String varName, String cExpr) {
		code.append(varName).append(" = ").append(cExpr).append(";\n");
	}

	public void invokeWithResult(String varType, String varName, String procName, List<String> cExprs) {
		code.append(varType);
		code.append(" ");
		code.append(varName);
		code.append(" = ");
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

	public void openIfBlock(String cExpr) {
		code.append("if (").append(cExpr).append(") {\n");
		stack.push(code = new StringBuilder());
	}

	public void openElseIfBlock(String cExpr) {
		code.append("else if (").append(cExpr).append(") {\n");
		stack.push(code = new StringBuilder());
	}

	public void openElseBlock() {
		code.append("else {\n");
		stack.push(code = new StringBuilder());
	}

	public void openWhileBlock(String cExpr) {
		code.append("while (").append(cExpr).append(") {\n");
		stack.push(code = new StringBuilder());
	}

	public void openDeclarationBlock(String cRetType, String name, List<String> cParamTypes, List<String> paramNames) {
		if (cParamTypes.size() != paramNames.size()) {
			throw new IllegalStateException();
		}
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
		stack.push(code = new StringBuilder());
	}

	public String getCode() {
		return code.toString();
	}

}
