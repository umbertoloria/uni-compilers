package app.visitor.clanggenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EasyNamesManager {

	private static final String FORMAT = "_%s_%x";
	private static final Set<String> cReservedWords = new HashSet<>();

	static {
		cReservedWords.add("if");
		cReservedWords.add("else");
		cReservedWords.add("switch");
		cReservedWords.add("case");
		cReservedWords.add("default");
		cReservedWords.add("break");
		cReservedWords.add("int");
		cReservedWords.add("float");
		cReservedWords.add("char");
		cReservedWords.add("double");
		cReservedWords.add("long");
		cReservedWords.add("for");
		cReservedWords.add("while");
		cReservedWords.add("do");
		cReservedWords.add("void");
		cReservedWords.add("goto");
		cReservedWords.add("auto");
		cReservedWords.add("signed");
		cReservedWords.add("const");
		cReservedWords.add("extern");
		cReservedWords.add("register");
		cReservedWords.add("unsigned");
		cReservedWords.add("return");
		cReservedWords.add("continue");
		cReservedWords.add("enum");
		cReservedWords.add("sizeof");
		cReservedWords.add("struct");
		cReservedWords.add("typedef");
		cReservedWords.add("union");
		cReservedWords.add("volatile");
	}

	private Set<String> unusableNames;
	private int i = 0;

	private final Set<String> reservedWords = new HashSet<>();

	public EasyNamesManager(Set<String> unusableNames) {
		this.unusableNames = unusableNames;
		reservedWords.addAll(cReservedWords);
	}

	public String createName(String friendlyName) {
		String easyName = String.format(FORMAT, friendlyName, i++);
		while (unusableNames.contains(easyName))
			easyName = String.format(FORMAT, friendlyName, i++);
		return easyName;
	}

	public void addReservedWord(String newReservedWord) {
		if (!reservedWords.contains(newReservedWord)) {
			reservedWords.add(newReservedWord);
		} else {
			// Deve essere solo chiamato per stringhe come "int_float_string".
			throw new IllegalStateException();
		}
	}

	// Contiene per esempio "case" => "_case_2" oppure "int_float" => "_int_float_2"
	private Map<String, String> aliases = new HashMap<>();

	public String safeNameUsage(String name) {
		// Il parola "continue" in Toy è permessa il generatore C la trasforma in qualcosa simile a "_continue_3"
		// prima di ogni sua menzione ogni per dichiarazioni, assegnazioni di variabili o invocazioni di funzioni.
		if (reservedWords.contains(name)) {
			if (aliases.containsKey(name)) {
				return aliases.get(name);
			} else {
				String safeName = createName(name);
				aliases.put(name, safeName);
				return safeName;
			}
		} else {
			return name;
		}
	}

}
