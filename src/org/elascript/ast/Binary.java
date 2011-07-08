package org.elascript.ast;

import org.elascript.*;

public class Binary extends Expression {
	private Expression left;
	private Expression right;
	private BinaryOperator operator;

	public Binary(Expression left, Expression right, BinaryOperator operator,
			int line) {
		super(line);
		CodeContract.requiresArgumentNotNull(left, "left");
		CodeContract.requiresArgumentNotNull(right, "right");
		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getRight() {
		return right;
	}

	public BinaryOperator getOperator() {
		return operator;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
