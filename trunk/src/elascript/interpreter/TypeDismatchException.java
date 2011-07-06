package elascript.interpreter;

import elascript.*;

public final class TypeDismatchException extends ScriptException {
	public TypeDismatchException(String typeRequired) {
		super("Type dismatch, " + typeRequired + " expected.");
	}
}
