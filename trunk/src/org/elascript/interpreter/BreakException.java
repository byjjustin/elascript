package org.elascript.interpreter;

public final class BreakException extends ControlFlowException {
	public BreakException(int line, String message) {
		super(line, message);
	}
}
