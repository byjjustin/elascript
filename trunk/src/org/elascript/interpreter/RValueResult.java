package org.elascript.interpreter;

public class RValueResult extends ValueResult {
	private Object value;

	public RValueResult(Object value) {
		this.value = value;
	}

	@Override
	public Object getValue() {
		return value;
	}
}
