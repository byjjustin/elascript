package elascript;

public final class ParsingException extends ScriptException {
	private int line;

	public ParsingException(int line, String message) {
		super(message);
		this.line = line;
	}

	public int getLine() {
		return line;
	}
}
