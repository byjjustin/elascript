package org.elascript.symbol;

import org.elascript.*;

public class Symbol {
	private String name;
	private Scope scope;

	public Symbol(String name) {
		CodeContract.requiresArgumentNotNull(name, "name");
		this.name = name;
	}

	public String getName() {
		return name;
	}

	void setScope(Scope scope) {
		if (this.scope != null && this.scope != scope)
			throw new IllegalStateException(
					"The symbol belongs to another scope.");
		this.scope = scope;
	}

	public Scope getScope() {
		return scope;
	}
}
