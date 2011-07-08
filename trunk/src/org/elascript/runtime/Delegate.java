package org.elascript.runtime;

import org.elascript.*;
import org.elascript.interpreter.*;

public abstract class Delegate {
	public abstract Object execute(Object args[]) throws FunctionCallException,
			InterpretingException;
}
