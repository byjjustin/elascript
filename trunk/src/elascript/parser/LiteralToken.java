package elascript.parser;

public class LiteralToken extends Token {
	private Object literal;

	public LiteralToken(Object literal) {
		super(literal.toString());
		this.literal = literal;
	}

	public Object getLiteral() {
		return literal;
	}
}
