package org.elascript.ast;

import org.elascript.*;

public class Evaluation extends EmbeddedStatement {
	private Expression expr;

	public Evaluation(Expression expr, int line) {
		super(line);
		CodeContract.requiresArgumentNotNull(expr, "expr");
		this.expr = expr;
	}

	public Expression getExpression() {
		return expr;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
