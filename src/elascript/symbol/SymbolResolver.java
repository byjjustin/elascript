package elascript.symbol;

import elascript.*;
import elascript.ast.*;

public interface SymbolResolver {
	public void resolve(Program ast) throws ParsingException;
}
