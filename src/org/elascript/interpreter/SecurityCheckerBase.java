package org.elascript.interpreter;

import java.util.*;

import org.elascript.*;
import org.elascript.symbol.*;


public abstract class SecurityCheckerBase implements SecurityChecker {
	protected Set<Class<?>> rules = new HashSet<Class<?>>();

	public void addRule(Class<?> type) {
		CodeContract.requiresArgumentNotNull(type, "type");
		rules.add(type);
	}

	@Override
	public boolean check(Result result) throws AccessException {
		CodeContract.requiresArgumentNotNull(result, "result");
		Class<?> type = null;
		if (result instanceof TypeResult)
			type = ((TypeResult) result).getType();
		if (result instanceof ValueResult) {
			Object value = ((ValueResult) result).getValue();
			if (value != null)
				type = value.getClass();
		}
		if (type == null)
			return true;

		String packageName = type.getPackage().getName();
		if (packageName.equals(GlobalScope.DEFAULT_ELASCRIPT_PACKAGE)
				|| packageName.equals(GlobalScope.DEFAULT_JAVA_PACKAGE))
			return true;
		return checkInternal(type);
	}

	protected abstract boolean checkInternal(Class<?> type);
}
