package elascript.parser;

import elascript.*;

public abstract class LLParser implements Parser {
	private Lexer lexer;
	private Token lookahead;
	private int line;

	public LLParser(Lexer lexer) throws ParsingException {
		CodeContract.requiresArgumentNotNull(lexer, "lexer");
		this.lexer = lexer;
		consume();
	}

	protected int peekLine() {
		return line;
	}

	protected boolean peek(String str) {
		return lookahead.getValue().equals(str);
	}

	protected LLParser match(String str) throws ParsingException {
		if (!peek(str))
			throw new ParsingException(line, str + " expected.");
		consume();
		return this;
	}

	protected boolean tryMatch(String str) throws ParsingException {
		if (peek(str)) {
			consume();
			return true;
		}
		return false;
	}

	protected Token consume() throws ParsingException {
		Token origin = lookahead;
		line = lexer.peekLine();
		lookahead = lexer.nextToken();
		return origin;
	}
}
