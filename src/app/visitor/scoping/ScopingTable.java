package app.visitor.scoping;

import java.util.*;

public class ScopingTable {

	private final String scopeName;
	private ScopingTable parent;

	private Map<String, ScopingTable> childrens = new LinkedHashMap<>();

	private Map<String, String> decls = new HashMap<>();
	private Set<String> blacklistProcedures = new HashSet<>();

	public ScopingTable(String scopeName) {
		this.scopeName = scopeName;
	}

	// Tree
	public ScopingTable getParent() {
		return parent;
	}

	public void setParent(ScopingTable parent) {
		this.parent = parent;
	}

	public ScopingTable createChild(String name) {
		ScopingTable childScopingTable = new ScopingTable(name);
		childScopingTable.setParent(this);
		childrens.put(name, childScopingTable);
		return childScopingTable;
	}

	// Scoping BL
	public void declareVariable(String name, String type) {
		// type does not contains "->"
		if (decls.containsKey(name)) {
			throw new IllegalStateException("Variabile '" + name + "' dichiarata con nome occupato");
		} else {
			decls.put(name, type);
		}
	}

	public void declareProcedure(String name, String type) {
		// type contains "->"
		if (decls.containsKey(name)) {
			throw new IllegalStateException("Procedura '" + name + "' dichiarata con nome occupato");
		} else {
			decls.put(name, type);
			// try clean the procedures blacklist
			blacklistProcedures.remove(name);
		}
	}

	public void useVariable(String name) {
		if (isUnreachable(name)) {
			throw new IllegalStateException("Variabile '" + name + "' non dichiarata");
		}
	}

	public void useProcedure(String name) {
		if (isUnreachable(name)) {
			blacklistProcedures.add(name);
		}
	}

	private boolean isUnreachable(String name) {
		// TODO: forse si può ancora usare uno stack: quando chiudi uno scope fai galleggiare sopra la blacklist?
		//  ovviamente quando scendi non la fai scendere. Però è *sempre* vero che risolvendo le dipendenze nel
		//  padre, i figli che sono morti sarebbero soddifatti???
		ScopingTable t = this;
		while (t != null && !t.decls.containsKey(name))
			t = t.getParent();
		return t == null;
	}

	public void resolveUnmatchedUsages() {
		for (String childrenName : childrens.keySet()) {
			childrens.get(childrenName).resolveUnmatchedUsages();
		}
		for (String blacklistProcedure : blacklistProcedures) {
			if (parent == null || parent.isUnreachable(blacklistProcedure)) {
				throw new IllegalStateException("Dichiarazione mancante di '" + blacklistProcedure + "'");
			}
		}
	}

	// Type-checking BL
	public String getTypeInCloserScopeGoingUp(String name) {
		ScopingTable t = this;
		while (t != null && (!t.decls.containsKey(name) || t.decls.get(name) == null))
			t = t.getParent();
		// 't' non può essere NULL perché 'name' proviene dall'AST, che è già stato controllato dallo scoping visitor
		assert t != null;
		return t.decls.get(name);
	}

	public ScopingTable getScopingTableOf(String name) {
		return childrens.get(name);
	}

	// Code-generation BL
	public Set<String> getAllNames() {
		Set<String> localSet = new HashSet<>(decls.keySet());
		for (String childScopeName : childrens.keySet()) {
			Set<String> childNames = childrens.get(childScopeName).getAllNames();
			localSet.addAll(childNames);
		}
		return localSet;
	}

	@Override
	public String toString() {
		return scopeName;
	}

}
