package elascript.interpreter;

import elascript.*;
import elascript.ast.*;

public interface Interpreter {
	public abstract void execute(Program ast) throws InterpretingException;

	public abstract Object execute(Function ast, MemorySpace env, Object[] args)
			throws FunctionCallException, InterpretingException;
}
