package org.elascript.interpreter;

public interface SecurityChecker {
	public boolean check(Result result) throws AccessException;
}
