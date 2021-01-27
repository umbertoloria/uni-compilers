package app.visitor.clanggenerator;

import java.util.Set;

public class TmpVarNameGenerator {

	private static final String FORMAT = "_%s_%x";

	private Set<String> unusableNames;
	private int i = 0;

	public TmpVarNameGenerator(Set<String> unusableNames) {
		this.unusableNames = unusableNames;
	}

	public String newName(String friendlyName) {
		String newName = String.format(FORMAT, friendlyName, i++);
		while (unusableNames.contains(newName))
			newName = String.format(FORMAT, friendlyName, i++);
		return newName;
	}

}
