package app.visitor;

import app.node.TypeNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TypeUtils {

	private static final String INT = TypeNode.INT.getStringType();
	private static final String FLOAT = TypeNode.FLOAT.getStringType();
	private static final String BOOL = TypeNode.BOOL.getStringType();
	private static final String STRING = TypeNode.STRING.toString();
	public static final Map<String, Set<String>> compatTypeSets = new HashMap<>();

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
	}

	public String getIntType() {
		return INT;
	}

	public String getFloatType() {
		return FLOAT;
	}

	public String getBoolType() {
		return BOOL;
	}

	public String getStringType() {
		return STRING;
	}

	public boolean isInt(String type) {
		return type.equals(INT);
	}

	public boolean isFloat(String type) {
		return type.equals(FLOAT);
	}

	public boolean isBool(String type) {
		return type.equals(BOOL);
	}

	public boolean isString(String type) {
		return type.equals(STRING);
	}

	public boolean isNumber(String type) {
		return isInt(type) || isFloat(type);
	}

	public boolean canAssign(String actualParamTypes, String formalParamTypes) {
		// TODO: tutti possono dare nullini? o solo CallProcOP
		if (formalParamTypes.isEmpty() || actualParamTypes.isEmpty()) {
			return formalParamTypes.isEmpty() && actualParamTypes.isEmpty();
		} else {
			String[] actualTypes = actualParamTypes.split(",");
			String[] formalTypes = formalParamTypes.split(",");
			if (actualTypes.length != formalTypes.length) {
				return false;
			}
			for (int i = 0; i < actualTypes.length; i++) {
				if (!compatTypeSets.get(actualTypes[i]).contains(formalTypes[i])) {
					return false;
				}
			}
			return true;
		}
	}

	public void assertCanCompare(String typeA, String typeB) {
		if ((!isNumber(typeA) || !isNumber(typeB)) && (!isString(typeA) || !isString(typeB))) {
			throw new IllegalStateException("Types '" + typeA + "' and '" + typeB + "' not suitable for comparison");
		}
	}

	public String assertUminusAndGetType(String type) {
		if (isInt(type)) {
			return getIntType();
		} else if (isFloat(type)) {
			return getFloatType();
		} else {
			throw new IllegalStateException("Type '" + type + "' not suitable for uminus");
		}
	}

	public void assertCanNegate(String type) {
		if (!isBool(type)) {
			throw new IllegalStateException("Type '" + type + "' not suitable for negation");
		}
	}


	public void assertCanAssign(String exprType, String idType) {
		// TODO: però nell'AssignOP non è possibile "" e ""
		if (!exprType.isEmpty() || !idType.isEmpty()) {
			if (!canAssign(exprType, idType)) {
				throw new IllegalStateException("type mismatch");
				// TODO: too generic message     ^^^^^^^^^^^^^
			/*throw new IllegalStateException("Type mismatch in return '" + procOP.id.name
					+ "' procedure: formal '" + formalRetExprStringTypes + "' " + "but actual '"
					+ actualRetExprTypes + "'");*/
			/*
			if (!typeTable.canAssign(exprsType, idsType)) {
				// TODO: implement compatibilities or implicit conversions (batched string type...)
				String idList = assignOP.ids.stream().map(id -> id.name).collect(Collectors.joining(","));
				throw new IllegalStateException("Type mismatch in assignment: having '" + idList + "' left '" + idsType
						+ "' but right '" + exprsType + "'");
			}
			*/
			/*
			if (!typeTable.canAssign(actuParamsType, formalParamsType)) {
				throw new IllegalStateException("Badly typed invocation to '" + callProcOP.procId.name
						+ "': expected '" + formalParamsType + "' but found '" + actuParamsType + "'");
			}*/
			/*
			if (formalParamTypes == null || actualParamTypes == null) {
				if (formalParamTypes != null || actualParamTypes != null) {
					throw new IllegalStateException("Badly typed invocation to '" + callProcOP.procId.name
							+ "': expected '" + formalParamTypes + "' but found '" + actualParamTypes + "'");
				}
			} else {
				if (!formalParamTypes.canContain(actualParamTypes)) {
					throw new IllegalStateException("Badly typed invocation to '" + callProcOP.procId.name
							+ "': expected '" + formalParamTypes + "' but found '" + actualParamTypes + "'");
				}
			}*/
			}
		}
	}

	public void assertCanWrite(String type) {
		// TODO: tutto convertibile in stringa o tutto *scrivibile*?
		/*
		if (!...(type)) {
			// TODO: maybe not only strings? passare per conversioni oppure gestire il solo caso di conversione
			//  implicita float->string (esempio) direttamente qui? Forse si può usare in altre parti?
			throw new IllegalStateException("writing expressions must be strings");
		}*/
	}

}
