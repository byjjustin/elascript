package elascript.runtime;

import elascript.*;
import elascript.interpreter.*;

public abstract class Delegate {
	public abstract Object execute(Object args[]) throws FunctionCallException,
			InterpretingException;
}
