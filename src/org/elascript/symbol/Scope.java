package org.elascript.symbol;

public interface Scope {
	public Scope getEnclosingScope();

	public void define(Symbol sym) throws SymbolException;

	public Symbol resolve(String name) throws SymbolException;

	public Iterable<Symbol> getSymbols();
}
