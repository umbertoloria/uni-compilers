package app.visitor.clanggenerator;

import app.node.TypeNode;

import java.util.HashMap;
import java.util.Map;

public class TypeCodifier {

	public static final String VOID = "void";
	private static final Map<String, String> typesFromAgnosticToC = new HashMap<>();
	public static final Map<String, String> defaultValueForTypes = new HashMap<>();

	static {
		typesFromAgnosticToC.put(TypeNode.INT.getStringType(), "int");
		typesFromAgnosticToC.put(TypeNode.FLOAT.getStringType(), "float");
		typesFromAgnosticToC.put(TypeNode.BOOL.getStringType(), "int");
		typesFromAgnosticToC.put(TypeNode.STRING.getStringType(), "char*");
		defaultValueForTypes.put("int", "0");
		defaultValueForTypes.put("float", "0");
		defaultValueForTypes.put("char*", "\"\"");
	}

	public String codify(TypeNode typeNode) {
		return typesFromAgnosticToC.get(typeNode.getStringType());
	}

}
