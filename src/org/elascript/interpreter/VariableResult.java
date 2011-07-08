package org.elascript.interpreter;

import org.elascript.*;
import org.elascript.symbol.*;

public class VariableResult extends LValueResult {
	private VariableSymbol sym;
	private MemorySpace env;

	public VariableResult(VariableSymbol sym, MemorySpace env) {
		CodeContract.requiresArgumentNotNull(sym, "sym");
		CodeContract.requiresArgumentNotNull(env, "env");
		this.sym = sym;
		this.env = env;
	}

	@Override
	public void setValue(Object value) {
		env.put(sym, value);
	}

	@Override
	public Object getValue() {
		return env.get(sym);
	}
}
