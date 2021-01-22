package app.visitor;

import app.node.*;
import app.node.binop.*;
import app.node.expr.*;
import app.node.stat.*;
import app.visitor.scope.ScopingTable;

import java.lang.reflect.Type;
import java.util.*;

public class TypeCheckingVisitor implements INodeVisitor {

	private ScopingTable scopingTable;

	public TypeCheckingVisitor(ScopingTable scopingTable) {
		this.scopingTable = scopingTable;
	}

	@Override
	public Object visitProgramOP(ProgramOP programOP) {
		if (programOP.getVarDecls() != null) {
			for (VarDeclOP varDecl : programOP.getVarDecls()) {
				varDecl.accept(this);
			}
		}
		for (ProcOP proc : programOP.getProcs()) {
			proc.accept(this);
		}
		return null;
	}

	@Override
	public Object visitVarDeclOP(VarDeclOP varDeclOP) {
		for (IdInitOP idInit : varDeclOP.getIdInits()) {
			if (idInit.getExpr() != null) {
				String varType = varDeclOP.getType().getStringType();
				String exprType = (String) idInit.getExpr().accept(this);
				if (!canPutValueInside(exprType, varType)) {
					throw new IllegalStateException("type mismatch");
				}
			}
		}
		return null;
	}

	@Override
	public Object visitTypeNode(TypeNode typeNode) {
		return null;
	}

	@Override
	public Object visitIdInitOP(IdInitOP idInitOP) {
		return null;
	}

	@Override
	public Object visitProcOP(ProcOP procOP) {
		scopingTable = scopingTable.getChildByName(procOP.getId().getName());
		String formalRetExprStringTypes = scopingTable.getStringType_ofParents_Of(procOP.getId().getName());
		formalRetExprStringTypes = formalRetExprStringTypes.substring(formalRetExprStringTypes.indexOf("->") + 2);
//		if (formalRetExprStringTypes.contains(TypeNode.STRING.getStringType()) && formalRetExprStringTypes.equals
//		(TypeNode.STRING.getStringType())) {
//			throw new IllegalStateException("Only one alone 'void' is allowed");
//		}XXXXXXXXXXXXXXXX
		String actualRetExprStringTypes = (String) procOP.getProcBody().accept(this);
//		if (!formalRetExprStringTypes.equals(actualRetExprStringTypes)) {
		if (!canPutValueInside(actualRetExprStringTypes, formalRetExprStringTypes)) {
			throw new IllegalStateException("Type mismatch in return '" + procOP.getId().getName()
					+ "' procedure: formal '" + formalRetExprStringTypes + "' " + "but actual '"
					+ actualRetExprStringTypes + "'");
		}
		// TODO: no sense void,void,float: only void or nothing.
		// TODO: idk if loopup the scoping table to see if it is in. It's in right *because* of the the exsistence of
		//  this procedure...
		/*for (ResultTypeNode returnType : procOP.getReturnTypes()) {
		}*/
		scopingTable = scopingTable.getParent();
		return null;
	}

	@Override
	public Object visitParDeclOP(ParDeclOP parDeclOP) {
		return null;
	}

	@Override
	public Object visitResultTypeNode(ResultTypeNode resultTypeNode) {
		return null;
	}

	@Override
	public Object visitProcBodyOP(ProcBodyOP procBodyOP) {
		if (procBodyOP.getVarDecls() != null) {
			for (VarDeclOP varDecl : procBodyOP.getVarDecls()) {
				varDecl.accept(this);
			}
		}
		if (procBodyOP.getBody() != null) {
			procBodyOP.getBody().accept(this);
		}
		if (procBodyOP.getReturnExpressions() != null) {
			List<String> returnTypes = new LinkedList<>();
			for (ExprNode returnExpression : procBodyOP.getReturnExpressions()) {
				String retExprType = (String) returnExpression.accept(this);
				returnTypes.add(retExprType);
			}
			return String.join(",", returnTypes);
		} else {
			return ResultTypeNode.VOID.getStringType();
		}
	}

	@Override
	public Object visitBodyOP(BodyOP bodyOP) {
		for (StatNode stmt : bodyOP.getStmts()) {
			stmt.accept(this);
		}
		return null;
	}

	@Override
	public Object visitIfOP(IfOP ifOP) {
		String exprType = (String) ifOP.getExpr().accept(this);
//		if (!TypeNode.BOOL.getStringType().equals(exprType)) {
		if (!canPutValueInside(exprType, TypeNode.BOOL.getStringType())) {
			throw new IllegalStateException("expression must be boolean");
		}
		ifOP.getIfBody().accept(this);
		if (ifOP.getElifs() != null) {
			for (ElifOP elif : ifOP.getElifs()) {
				elif.accept(this);
			}
		}
		if (ifOP.getElseBody() != null) {
			ifOP.getElseBody().accept(this);
		}
		return null;
	}

	@Override
	public Object visitElifOP(ElifOP elifOP) {
		String exprType = (String) elifOP.getExpr().accept(this);
//		if (!TypeNode.BOOL.getStringType().equals(exprType)) {
		if (!canPutValueInside(exprType, TypeNode.BOOL.getStringType())) {
			throw new IllegalStateException("expression must be boolean");
		}
		elifOP.getBody().accept(this);
		return null;
	}

	@Override
	public Object visitWhileOP(WhileOP whileOP) {
		if (whileOP.getPreStmts() != null) {
			whileOP.getPreStmts().accept(this);
		}
		String exprType = (String) whileOP.getExpr().accept(this);
//		if (!TypeNode.BOOL.getStringType().equals(exprType)) {
		if (!canPutValueInside(exprType, TypeNode.BOOL.getStringType())) {
			throw new IllegalStateException("expression must be boolean");
		}
		whileOP.getIterStmts().accept(this);
		return null;
	}

	@Override
	public Object visitAssignOP(AssignOP assignOP) {
		// TODO: return the type of the assigned?
		StringBuilder stringTypesBuilder = new StringBuilder();
		for (Id id : assignOP.getIds()) {
			String stringType = (String) id.accept(this);
			stringTypesBuilder.append(stringType);
			stringTypesBuilder.append(',');
		}
		stringTypesBuilder.deleteCharAt(stringTypesBuilder.length() - 1);
		String idStringTypes = stringTypesBuilder.toString();
		stringTypesBuilder.delete(0, stringTypesBuilder.length());
		for (ExprNode expr : assignOP.getExprs()) {
			String stringType = (String) expr.accept(this);
			stringTypesBuilder.append(stringType);
			stringTypesBuilder.append(',');
		}
		stringTypesBuilder.deleteCharAt(stringTypesBuilder.length() - 1);
		String exprStringTypes = stringTypesBuilder.toString();
//		if (!idStringTypes.equals(exprStringTypes)) {
		if (!canPutValueInside(exprStringTypes, idStringTypes)) {
			// TODO: implement compatibilities or implicit conversions (batched string type...)
			StringBuilder idList = new StringBuilder();
			for (Id id : assignOP.getIds()) {
				idList.append(id.getName());
				idList.append(',');
			}
			idList.deleteCharAt(idList.length() - 1);
			throw new IllegalStateException("Type mismatch in assignment: having '" + idList.toString() + "' left '"
					+ idStringTypes + "' but right '" + exprStringTypes + "'");
		}
		return null;
	}

	@Override
	public Object visitReadlnOP(ReadlnOP readlnOP) {
		// TODO: only certain types?
		return null;
	}

	@Override
	public Object visitWriteOP(WriteOP writeOP) {
		if (writeOP.getExprs() != null) {
			for (ExprNode expr : writeOP.getExprs()) {
				String stringType = (String) expr.accept(this);
//				if (!stringType.equals(TypeNode.STRING.getStringType())) {
				if (!canPutValueInside(stringType, TypeNode.STRING.getStringType())) {
					// TODO: maybe not only strings? passare per conversioni oppure gestire il solo caso di conversione
					//  implicita float->string (esempio) direttamente qui? Forse si può usare in altre parti?
					throw new IllegalStateException("writing expressions must be strings");
				}
			}
		}
		// TODO: only strings or convertible strings?
		return null;
	}

	@Override
	public Object visitId(Id id) {
		return scopingTable.getStringType_ofParents_Of(id.getName());
	}

	@Override
	public Object visitNull(Null aNull) {
		// TODO: NULL type, what?
		return "null";
	}

	@Override
	public Object visitTrue(True aTrue) {
		return TypeNode.BOOL.getStringType();
	}

	@Override
	public Object visitFalse(False aFalse) {
		return TypeNode.BOOL.getStringType();
	}

	@Override
	public Object visitIntConst(IntConst intConst) {
		return TypeNode.INT.getStringType();
	}

	@Override
	public Object visitFloatConst(FloatConst floatConst) {
		return TypeNode.FLOAT.getStringType();
	}

	@Override
	public Object visitStringConst(StringConst stringConst) {
		return TypeNode.STRING.getStringType();
	}

	@Override
	public Object visitNotOP(NotOP notOP) {
		String type = (String) notOP.getExpr().accept(this);
		if (type.equals(TypeNode.BOOL.getStringType())) {
			return TypeNode.BOOL.getStringType();
		} else {
			// FIXME: msgs...
			throw new IllegalStateException("MUST BE BOOL TO NEGATE set some conversion tables...");
		}
	}

	@Override
	public Object visitUMinusOP(UMinusOP uMinusOP) {
		String stringType = (String) uMinusOP.getExpr().accept(this);
		if (stringType.equals(TypeNode.INT.getStringType()) || stringType.equals(TypeNode.FLOAT.getStringType())) {
			return stringType;
		} else {
			throw new IllegalStateException("set some conversion tables...");
		}
	}

	@Override
	public Object visitCallProcOP(CallProcOP callProcOP) {

		// TODO: procedure type?
		// formal params types
		// FIXME: use "Most-closely nested rule" as name of some (top) recoursive methods
		String procStringType = scopingTable.getStringType_ofParents_Of(callProcOP.getProcId().getName());
		String procLeftStringTypes = procStringType.substring(0, procStringType.indexOf("->"));
		// actual params types
		StringBuilder paramsStringBuilderTypes = new StringBuilder();
		if (callProcOP.getExprs() != null) {
			for (ExprNode expr : callProcOP.getExprs()) {
				String stringType = (String) expr.accept(this);
				paramsStringBuilderTypes.append(stringType);
				paramsStringBuilderTypes.append(',');
			}
			paramsStringBuilderTypes.deleteCharAt(paramsStringBuilderTypes.length() - 1);
		}
		String paramsStringTypes = paramsStringBuilderTypes.toString();
		if (procLeftStringTypes.isEmpty() || callProcOP.getExprs() == null) {
			if (!procLeftStringTypes.isEmpty() || callProcOP.getExprs() != null) {
				throw new IllegalStateException("Badly typed invocation to '" + callProcOP.getProcId().getName()
						+ "': expected '" + procLeftStringTypes + "' but found '" + paramsStringTypes + "'");
			}
		} else {
//		    if (!canPutValueInside(stringType, TypeNode.STRING.getStringType())) {
			if (!canPutValueInside(paramsStringTypes, procLeftStringTypes)) {
				throw new IllegalStateException("Badly typed invocation to '" + callProcOP.getProcId().getName()
						+ "': expected '" + procLeftStringTypes + "' but found '" + paramsStringTypes + "'");
			}
		}
		return procStringType.substring(procLeftStringTypes.length() + 2);
	}

	@Override
	public Object visitLTOP(LTOP ltop) {
		String typeA = (String) ltop.getA().accept(this);
		String typeB = (String) ltop.getB().accept(this);
		return retBoolIfComparableNumbersOrStrsOrThrow(typeA, typeB);
	}

	@Override
	public Object visitLEOP(LEOP leop) {
		String typeA = (String) leop.getA().accept(this);
		String typeB = (String) leop.getB().accept(this);
		return retBoolIfComparableNumbersOrStrsOrThrow(typeA, typeB);
	}

	@Override
	public Object visitGTOP(GTOP gtop) {
		String typeA = (String) gtop.getA().accept(this);
		String typeB = (String) gtop.getB().accept(this);
		return retBoolIfComparableNumbersOrStrsOrThrow(typeA, typeB);
	}

	@Override
	public Object visitGEOP(GEOP geop) {
		String typeA = (String) geop.getA().accept(this);
		String typeB = (String) geop.getB().accept(this);
		return retBoolIfComparableNumbersOrStrsOrThrow(typeA, typeB);
	}

	@Override
	public Object visitEQOP(EQOP eqop) {
		String typeA = (String) eqop.getA().accept(this);
		String typeB = (String) eqop.getB().accept(this);
		return retBoolIfComparableNumbersOrStrsOrThrow(typeA, typeB);
	}

	@Override
	public Object visitAndOP(AndOP andOP) {
		String typeA = (String) andOP.getA().accept(this);
		String typeB = (String) andOP.getB().accept(this);
		return retBoolIfBoolOrThrow(typeA, typeB);
	}

	@Override
	public Object visitOrOP(OrOP orOP) {
		String typeA = (String) orOP.getA().accept(this);
		String typeB = (String) orOP.getB().accept(this);
		return retBoolIfBoolOrThrow(typeA, typeB);
	}

	@Override
	public Object visitTimesOP(TimesOP timesOP) {
		String typeA = (String) timesOP.getA().accept(this);
		String typeB = (String) timesOP.getB().accept(this);
		return getCommonTypeForAritBinaryExprOrThrow(typeA, typeB);
	}

	@Override
	public Object visitDivOP(DivOP divOP) {
		String typeA = (String) divOP.getA().accept(this);
		String typeB = (String) divOP.getB().accept(this);
		return getCommonTypeForAritBinaryExprOrThrow(typeA, typeB);
	}

	@Override
	public Object visitPlusOP(PlusOP plusOP) {
		String typeA = (String) plusOP.getA().accept(this);
		String typeB = (String) plusOP.getB().accept(this);
		return getCommonTypeForAritBinaryExprOrThrow(typeA, typeB);
	}

	@Override
	public Object visitMinusOP(MinusOP minusOP) {
		String typeA = (String) minusOP.getA().accept(this);
		String typeB = (String) minusOP.getB().accept(this);
		return getCommonTypeForAritBinaryExprOrThrow(typeA, typeB);
	}

	private Object retBoolIfComparableNumbersOrStrsOrThrow(String typeA, String typeB) {
		// FIXME: sure works everytime? Better impl.?
		Set<String> numberTypes = new HashSet<>();
		numberTypes.add(TypeNode.INT.getStringType());
		numberTypes.add(TypeNode.FLOAT.getStringType());
		if (numberTypes.contains(typeA) && numberTypes.contains(typeB)) {
			return TypeNode.BOOL.getStringType();
		} else if (typeA.equals(typeB) && typeA.equals(TypeNode.STRING.getStringType())) {
			return TypeNode.STRING.getStringType();
		} else {
			throw new IllegalStateException("set some conversion tables...");
		}
	}

	private String getCommonTypeForAritBinaryExprOrThrow(String typeA, String typeB) {
		// FIXME: sure works everytime? Better impl.?
		Set<String> numberTypes = new HashSet<>();
		numberTypes.add(TypeNode.INT.getStringType());
		numberTypes.add(TypeNode.FLOAT.getStringType());
		if (numberTypes.contains(typeA) && numberTypes.contains(typeB)) {
			if (typeA.equals(typeB) && typeA.equals(TypeNode.INT.getStringType())) {
				return TypeNode.INT.getStringType();
			} else {
				return TypeNode.FLOAT.getStringType();
			}
		} else {
			throw new IllegalStateException("set some conversion tables...");
		}
	}

	private String retBoolIfBoolOrThrow(String typeA, String typeB) {
		if (typeA.equals(typeB) && TypeNode.BOOL.getStringType().equals(typeA)) {
			return TypeNode.BOOL.getStringType();
		} else {
			// FIXME: msgs...
			throw new IllegalStateException("MUST BE BOOL set some conversion tables...");
		}
	}

	private static final Map<String, Set<String>> compatTypeSets = new HashMap<>();

	static {
		Set<String> intCompTypes = new HashSet<>();
		intCompTypes.add(TypeNode.INT.getStringType());
		intCompTypes.add(TypeNode.FLOAT.getStringType());
		intCompTypes.add(TypeNode.STRING.getStringType());
		compatTypeSets.put(TypeNode.INT.getStringType(), intCompTypes);
		Set<String> floatCompTypes = new HashSet<>();
		floatCompTypes.add(TypeNode.FLOAT.getStringType());
		floatCompTypes.add(TypeNode.STRING.getStringType());
		compatTypeSets.put(TypeNode.FLOAT.getStringType(), floatCompTypes);
		Set<String> stringCompTypes = new HashSet<>();
		stringCompTypes.add(TypeNode.STRING.getStringType());
		compatTypeSets.put(TypeNode.STRING.getStringType(), stringCompTypes);
		Set<String> boolCompTypes = new HashSet<>();
		boolCompTypes.add(TypeNode.BOOL.getStringType());
		compatTypeSets.put(TypeNode.BOOL.getStringType(), boolCompTypes);
		Set<String> voidCompTypes = new HashSet<>();
		voidCompTypes.add(ResultTypeNode.VOID.getStringType());
		compatTypeSets.put(ResultTypeNode.VOID.getStringType(), voidCompTypes);
	}

	private boolean canPutValueInside(String typeSrc, String typeDest) {
		String[] srcParts = typeSrc.split(",");
		String[] destParts = typeDest.split(",");
		if (srcParts.length != destParts.length) {
			return false;
		} else {
			int i;
			for (i = 0; i < srcParts.length; i++) {
				if (!compatTypeSets.get(srcParts[i]).contains(destParts[i])) {
					return false;
				}
			}
			return true;
		}
	}

}
