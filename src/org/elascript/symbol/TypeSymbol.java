package org.elascript.symbol;

public class TypeSymbol extends Symbol {
	private Class<?> type;

	public TypeSymbol(Class<?> type) {
		super(type.getSimpleName());
		this.type = type;
	}

	public Class<?> getType() {
		return type;
	}
}
