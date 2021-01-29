package app.visitor.typechecking;

import app.node.TypeNode;
import app.visitor.ErrorsManager;

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
		compatTypeSets.put(TypeNode.INT.getStringType(), intCompTypes);
		Set<String> floatCompTypes = new HashSet<>();
		floatCompTypes.add(TypeNode.FLOAT.getStringType());
		compatTypeSets.put(TypeNode.FLOAT.getStringType(), floatCompTypes);
		Set<String> stringCompTypes = new HashSet<>();
		stringCompTypes.add(TypeNode.STRING.getStringType());
		compatTypeSets.put(TypeNode.STRING.getStringType(), stringCompTypes);
		Set<String> boolCompTypes = new HashSet<>();
		boolCompTypes.add(TypeNode.BOOL.getStringType());
		compatTypeSets.put(TypeNode.BOOL.getStringType(), boolCompTypes);
	}

	private ErrorsManager errorsManager = new ErrorsManager();

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

	public void assertCanCompare(String typeA, String typeB) {
		if (!(isNumber(typeA) && isNumber(typeB)) && !(isString(typeA) && isString(typeB))
				&& !(isBool(typeA) && isBool(typeB))) {
			errorsManager.typeMismatchInComparison(typeA, typeB);
		}
	}

	public String assertUminusAndGetType(String type) {
		if (isInt(type)) {
			return getIntType();
		} else if (isFloat(type)) {
			return getFloatType();
		} else {
			errorsManager.typeMismatchInUminus(type);
			return null;
		}
	}

	public void assertCanNegate(String type) {
		if (!isBool(type)) {
			errorsManager.typeMismatchInNegation(type);
		}
	}

	public void assertCanAssign(String actualParamTypes, String formalParamTypes) {
		if (!actualParamTypes.isEmpty() || !formalParamTypes.isEmpty()) {
			// Se almeno uno è non vuoto
			if (!formalParamTypes.isEmpty() && !actualParamTypes.isEmpty()) {
				// Se sono entrambi non vuoti
				String[] actualTypes = actualParamTypes.split(",");
				String[] formalTypes = formalParamTypes.split(",");
				if (actualTypes.length != formalTypes.length) {
					errorsManager.typeMismatchInAssign(actualParamTypes, formalParamTypes);
				} else {
					for (int i = 0; i < actualTypes.length; i++) {
						if (!compatTypeSets.get(actualTypes[i]).contains(formalTypes[i])) {
							errorsManager.typeMismatchInAssign(actualParamTypes, formalParamTypes);
							break;
						}
					}
				}
			} else {
				errorsManager.typeMismatchInAssign(actualParamTypes, formalParamTypes);
			}
		}
	}

}
