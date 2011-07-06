package elascript.symbol;

import elascript.*;

public final class SymbolException extends ScriptException {
	private Symbol sym;

	public SymbolException(Symbol sym, String message) {
		super(message);
		this.sym = sym;
	}

	public SymbolException(String message) {
		this(null, message);
	}

	public Symbol getSymbol() {
		return sym;
	}
}
