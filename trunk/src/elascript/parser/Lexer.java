package elascript.parser;

import elascript.*;

public interface Lexer {
	public Token nextToken() throws ParsingException;
	
	public int peekLine();
}
