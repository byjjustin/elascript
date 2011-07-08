package org.elascript.symbol;

import java.util.*;

import org.elascript.*;


public class LocalScope implements Scope {
	private Scope enclosingScope;
	private Map<String, Symbol> symbols = new Hashtable<String, Symbol>();

	public LocalScope(Scope enclosingScope) {
		this.enclosingScope = enclosingScope;
	}

	@Override
	public Scope getEnclosingScope() {
		return enclosingScope;
	}

	@Override
	public void define(Symbol sym) throws SymbolException {
		CodeContract.requiresArgumentNotNull(sym, "sym");
		if (symbols.containsKey(sym.getName()))
			throw new SymbolException(sym, "The symbol " + sym.getName()
					+ " has been defined in the scope.");
		symbols.put(sym.getName(), sym);
		sym.setScope(this);
	}

	@Override
	public Symbol resolve(String name) throws SymbolException {
		CodeContract.requiresArgumentNotNull(name, "name");
		Symbol sym = symbols.get(name);
		if (sym != null)
			return sym;
		if (enclosingScope != null)
			return enclosingScope.resolve(name);
		return null;
	}

	@Override
	public Iterable<Symbol> getSymbols() {
		return symbols.values();
	}
}
