package app.visitor.scope;

import java.util.*;

public class ScopingTable {

	private final String scopeName;
	private ScopingTable parent;

	// FIXME: order is important?
	private Map<String, ScopingTable> childrens = new HashMap<>();
	private List<String> childrensNames = new LinkedList<>();

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

	/*public List<ScopingTable> getChildrens() {
		return childrens;
	}*/

	public void appendChild(ScopingTable child) {
		childrensNames.add(child.scopeName);
		childrens.put(child.scopeName, child);
	}

	// BL
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
		// ovviamente quando scendi non la fai scendere. Però è *sempre* vero che risolvendo le dipendenze nel padre, i
		// figli che sono morti sarebbero soddifatti?
		ScopingTable t = this;
		while (t != null && (!t.decls.containsKey(name) || t.decls.get(name) == null))
			t = t.getParent();
		return t == null;
	}

	/*public void resolveUnmatchedUsages() {
		for (String blacklistProcedure : blacklistProcedures) {
			if (parent == null || parent.isUnreachable(blacklistProcedure)) {
				throw new IllegalStateException("Dichiarazione mancante di '" + blacklistProcedure + "'");
			}
		}
	}*/

	public void resolveUnmatchedUsages() {
		for (String childrensName : childrensNames) {
			childrens.get(childrensName).resolveUnmatchedUsages();
		}
		for (String blacklistProcedure : blacklistProcedures) {
			if (parent == null || parent.isUnreachable(blacklistProcedure)) {
				throw new IllegalStateException("Dichiarazione mancante di '" + blacklistProcedure + "'");
			}
		}
	}

	@Override
	public String toString() {
		return scopeName;
	}

	// for TypeCheckingVisitor
	public String getStringType_ofParents_Of(String name) {
		// FIXME: sure not to check? I thing it's here right *because* of the existence of this variable
		ScopingTable t = this;
		while (t != null && (!t.decls.containsKey(name) || t.decls.get(name) == null))
			t = t.getParent();
		if (t != null) {
			return t.decls.get(name);
		} else {
			// TODO: non può succedere nella fase di TypeChecking se tutto è andato bene in Scoping
			throw new IllegalStateException();
		}
	}

	public ScopingTable getChildByName(String name) {
		// TODO: otherwise throw exception?
		return childrens.get(name);
	}

	public ScopingTable createChild(String name) {
		ScopingTable childScopingTable = new ScopingTable(name);
		childScopingTable.setParent(this);
		childrensNames.add(name);
		childrens.put(name, childScopingTable);
		return childScopingTable;
	}
}
