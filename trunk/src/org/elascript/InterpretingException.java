package org.elascript;

public class InterpretingException extends ScriptException {
	private int line;

	public InterpretingException(int line, String message) {
		super(message);
		this.line = line;
	}

	public int getLine() {
		return line;
	}
}
