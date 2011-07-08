package org.elascript.interpreter;

public class BlackListChecker extends SecurityCheckerBase {
	@Override
	protected boolean checkInternal(Class<?> type) {
		return !rules.contains(type);
	}
}
