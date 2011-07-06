package elascript.interpreter;

public final class ContinueException extends ControlFlowException {
	public ContinueException(int line, String message) {
		super(line, message);
	}
}
