package elascript.interpreter;

import elascript.*;
import elascript.runtime.*;

public class ExternalDelegate extends Delegate {
	private Class<?> type;
	private String target;
	private Object instance;

	public ExternalDelegate(Class<?> type, String target, Object instance) {
		CodeContract.requiresArgumentNotNull(type, "type");
		CodeContract.requiresArgumentNotNull(target, "target");
		this.type = type;
		this.target = target;
		this.instance = instance;
	}

	@Override
	public Object execute(Object[] args) throws FunctionCallException {
		try {
			return TypeHelper.matchMethod(type, target, args).invoke(instance,
					args);
		} catch (Exception e) {
			throw new FunctionCallException(e.getMessage());
		}
	}
}
