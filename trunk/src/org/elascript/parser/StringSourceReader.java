package org.elascript.parser;

import org.elascript.*;

public class StringSourceReader extends SourceReader {
	private String source;
	private int current = 0;

	public StringSourceReader(String source) {
		CodeContract.requiresArgumentNotNull(source, "source");
		this.source = source;
	}

	@Override
	protected char readCharInternal() {
		if (current == source.length())
			return SourceReader.EOF;
		else
			return source.charAt(current++);
	}
}
