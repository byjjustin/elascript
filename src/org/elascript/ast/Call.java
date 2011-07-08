package org.elascript.ast;

import java.util.*;

import org.elascript.*;


public class Call extends Expression {
	private Expression function;
	private List<Expression> args;

	public Call(Expression function, List<Expression> args, int line) {
		super(line);
		CodeContract.requiresArgumentNotNull(function, "function");
		this.function = function;
		this.args = Utils.makeReadOnlyCopy(args);
	}

	public Expression getFunction() {
		return function;
	}

	public Iterable<Expression> getArguments() {
		return args;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
