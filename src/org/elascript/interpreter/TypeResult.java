package org.elascript.interpreter;

import org.elascript.*;

public class TypeResult extends Result {
	private Class<?> type;

	public TypeResult(Class<?> type) {
		CodeContract.requiresArgumentNotNull(type, "type");
		this.type = type;
	}

	public Class<?> getType() {
		return type;
	}
}
