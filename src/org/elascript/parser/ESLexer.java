package org.elascript.parser;

import java.util.*;

import org.elascript.*;


public class ESLexer extends LexerBase {
	private static Set<String> symbols = new HashSet<String>();
	private static Set<String> keywords = new HashSet<String>();
	private static Map<String, Object> constants = new HashMap<String, Object>();

	static {
		symbols.addAll(Arrays.asList("~", "!", "!=", "%", "%=", "^", "^=", "&",
				"&&", "&=", "*", "*=", "(", ")", "+", "+=", "++", "-", "-=",
				"--", "=", "==", "=>", "{", "}", "[", "]", "|", "||", "|=",
				";", ",", ".", "<", "<=", "<<", ">", ">=", ">>", ">>=", ">>>",
				">>>=", "/", "/=", ":", "$"));
		keywords.addAll(Arrays.asList("using", "var", "function", "if", "for",
				"foreach", "while", "do", "break", "continue", "return", "new",
				"is"));
		constants.put("true", true);
		constants.put("false", false);
		constants.put("null", null);
	}

	public ESLexer(SourceReader source) {
		super(source);
	}

	@Override
	public Token nextToken() throws ParsingException {
		while (peek() == ' ' || peek() == '\t' || peek() == '\n'
				|| peek() == '\r')
			consume();

		int line = peekLine();
		Token symbol = symbol();
		if (symbol != null)
			return symbol;
		if (Character.isDigit(peek()))
			return numberLiteral();
		if (peek() == '\'')
			return charLiteral();
		if (peek() == '"')
			return stringLiteral();
		if (peek() == '`')
			return rawStringLiteral();
		if (peek() == SourceReader.EOF)
			return new EOFToken();
		if (Character.isLetter(peek()))
			return identifierOrKeyword();
		throw new ParsingException(line, "Invalid character.");
	}

	private Token symbol() {
		String sym = "";
		while (symbols.contains(sym + peek()))
			sym += consume();
		if (!sym.isEmpty())
			return new Token(sym);
		return null;
	}

	private LiteralToken numberLiteral() {
		int integer = 0;
		do {
			integer = 10 * integer + consume() - '0';
		} while (Character.isDigit(peek()));
		if (peek() != '.')
			return new LiteralToken(integer);
		consume();
		double real = integer, d = 1;
		do {
			d *= 10;
			real = real + (consume() - '0') / d;
		} while (Character.isDigit(peek()));
		return new LiteralToken(real);
	}

	private LiteralToken charLiteral() throws ParsingException {
		match('\'');
		LiteralToken c = new LiteralToken(singleChar());
		match('\'');
		return c;
	}

	private LiteralToken stringLiteral() throws ParsingException {
		match('"');
		StringBuffer buffer = new StringBuffer();
		while (peek() != '"')
			buffer.append(singleChar());
		match('"');
		return new LiteralToken(buffer.toString());
	}

	private char singleChar() throws ParsingException {
		int line = peekLine();
		char c = consume();
		if (c == '\\') {
			c = consume();
			switch (c) {
			case 'b':
				return '\b';
			case 't':
				return '\t';
			case 'n':
				return '\n';
			case 'f':
				return '\f';
			case 'r':
				return '\r';
			case '\\':
				return '\\';
			case '\'':
				return '\'';
			case '"':
				return '"';
			case '?':
				return '?';
			default:
				throw new ParsingException(line, "Invalid escape sequence.");
			}
		}
		return c;
	}

	private LiteralToken rawStringLiteral() throws ParsingException {
		match('`');
		StringBuffer buffer = new StringBuffer();
		while (true) {
			while (peek() != '`')
				buffer.append(consume());
			match('`');
			if (peek() == '`')
				buffer.append(consume());
			else
				break;
		}
		return new LiteralToken(buffer.toString());
	}

	private Token identifierOrKeyword() {
		StringBuffer buffer = new StringBuffer();
		do {
			buffer.append(consume());
		} while (Character.isLetterOrDigit(peek()));
		String name = buffer.toString();
		if (keywords.contains(name))
			return new Token(name);
		if (constants.containsKey(name))
			return new LiteralToken(constants.get(name));
		return new IdentifierToken(name);
	}
}
