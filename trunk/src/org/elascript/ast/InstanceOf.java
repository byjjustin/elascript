package org.elascript.ast;

import org.elascript.*;

public class InstanceOf extends Expression {
	private Expression left;
	private Expression type;

	public InstanceOf(Expression left, Expression type, int line) {
		super(line);
		CodeContract.requiresArgumentNotNull(left, "left");
		CodeContract.requiresArgumentNotNull(type, "type");
		this.left = left;
		this.type = type;
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getType() {
		return type;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
