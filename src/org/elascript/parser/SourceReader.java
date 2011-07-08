package org.elascript.parser;

public abstract class SourceReader {
	public static final char EOF = Character.MIN_VALUE;
	private int line = 1;

	public char readChar() {
		char c = readCharInternal();
		if (c == '\n')
			++line;
		return c;
	}

	public int peekLine() {
		return line;
	}

	protected abstract char readCharInternal();
}
