package org.elascript.symbol;

import org.elascript.*;
import org.elascript.ast.*;

public interface SymbolResolver {
	public void resolve(Program ast) throws ParsingException;
}
