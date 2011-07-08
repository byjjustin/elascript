package org.elascript.parser;

import org.elascript.*;

public abstract class LexerBase implements Lexer {
	private SourceReader source;
	private char lookahead;
	private int line;

	public LexerBase(SourceReader source) {
		CodeContract.requiresArgumentNotNull(source, "source");
		this.source = source;
		consume();
	}

	@Override
	public int peekLine() {
		return line;
	}

	protected char peek() {
		return lookahead;
	}

	protected void match(char c) throws ParsingException {
		if (lookahead != c)
			throw new ParsingException(line, "Character " + c + " expected.");
		consume();
	}

	protected char consume() {
		char origin = lookahead;
		line = source.peekLine();
		lookahead = source.readChar();
		return origin;
	}
}
