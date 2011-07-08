package org.elascript.ast;

import org.elascript.*;

public class Unary extends Expression {
	private Expression operand;
	private UnaryOperator operator;

	public Unary(Expression operand, UnaryOperator operator, int line) {
		super(line);
		CodeContract.requiresArgumentNotNull(operand, "operand");
		this.operand = operand;
		this.operator = operator;
	}

	public Expression getOperand() {
		return operand;
	}

	public UnaryOperator getOperator() {
		return operator;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
