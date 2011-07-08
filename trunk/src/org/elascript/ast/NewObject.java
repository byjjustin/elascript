package org.elascript.ast;

import java.util.*;

import org.elascript.*;


public class NewObject extends Expression {
	private Expression type;
	private List<Expression> args;

	public NewObject(Expression type, List<Expression> args, int line) {
		super(line);
		CodeContract.requiresArgumentNotNull(type, "type");
		this.type = type;
		this.args = Utils.makeReadOnlyCopy(args);
	}

	public Expression getType() {
		return type;
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
