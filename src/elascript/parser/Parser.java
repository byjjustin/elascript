package elascript.parser;

import elascript.*;
import elascript.ast.*;

public interface Parser {
	public Program parse() throws ParsingException;
}
