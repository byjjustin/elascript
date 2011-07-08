package org.elascript.interpreter;

public final class ReturnException extends ControlFlowException {
	private Object returnValue;

	public ReturnException(Object returnValue, int line, String message) {
		super(line, message);
		this.returnValue = returnValue;
	}

	public Object getReturnValue() {
		return returnValue;
	}
}
