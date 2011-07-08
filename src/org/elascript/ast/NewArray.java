package org.elascript.ast;

import java.util.*;

import org.elascript.*;


public class NewArray extends Expression {
	private Expression type;
	private List<Expression> bounds;

	public NewArray(Expression type, List<Expression> bounds, int line) {
		super(line);
		CodeContract.requiresArgumentNotNull(type, "type");
		this.type = type;
		this.bounds = Utils.makeReadOnlyCopy(bounds);
		CodeContract.requires(!bounds.isEmpty(),
				"The bounds of the array cannot be empty.");
	}

	public Expression getType() {
		return type;
	}

	public Iterable<Expression> getBounds() {
		return bounds;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
