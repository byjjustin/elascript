package org.elascript;

import java.io.*;

public final class TestConsole {
	public static void main(String[] args) throws ParsingException,
			InterpretingException, IOException {
		StringBuffer source = new StringBuffer();
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		line = reader.readLine();
		while (line != null) {
			source.append(line);
			source.append("\n");
			line = reader.readLine();
		}
		ScriptEngine engine = new ScriptEngine(source.toString());
		engine.execute();
	}
}
