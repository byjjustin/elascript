package org.elascript.parser;

import org.elascript.*;
import org.elascript.ast.*;

public interface Parser {
	public Program parse() throws ParsingException;
}
