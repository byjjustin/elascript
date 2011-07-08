package org.elascript.interpreter;

public abstract class ValueResult extends Result {
	public abstract Object getValue() throws AccessException;
}
