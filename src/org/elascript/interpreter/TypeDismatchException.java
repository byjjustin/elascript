package org.elascript.interpreter;

import org.elascript.*;

public final class TypeDismatchException extends ScriptException {
	public TypeDismatchException(String typeRequired) {
		super("Type dismatch, " + typeRequired + " expected.");
	}
}
