package elascript.interpreter;

import elascript.*;

public abstract class ControlFlowException extends InterpretingException {
	public ControlFlowException(int line, String message) {
		super(line, message);
	}
}
