package org.elascript.parser;

public class EOFToken extends Token {
	public static final String EOF = "<EOF>";

	public EOFToken() {
		super(EOF);
	}
}
