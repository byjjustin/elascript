package org.elascript.interpreter;

import org.elascript.*;
import org.elascript.ast.*;
import org.elascript.runtime.*;

public class InternalDelegate extends Delegate {
	private Function ast;
	private MemorySpace env;

	public InternalDelegate(Function ast, MemorySpace env) {
		CodeContract.requiresArgumentNotNull(ast, "ast");
		CodeContract.requiresArgumentNotNull(env, "env");
		this.ast = ast;
		this.env = env;
	}

	@Override
	public Object execute(Object... args) throws FunctionCallException,
			InterpretingException {
		return env.getInterpreter().execute(ast, env, args);
	}
}
