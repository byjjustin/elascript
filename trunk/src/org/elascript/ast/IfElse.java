package org.elascript.ast;

import org.elascript.*;

public class IfElse extends EmbeddedStatement {
	private Expression cond;
	private EmbeddedStatement truePart;
	private EmbeddedStatement falsePart;

	public IfElse(Expression cond, EmbeddedStatement truePart,
			EmbeddedStatement falsePart, int line) {
		super(line);
		CodeContract.requiresArgumentNotNull(cond, "cond");
		this.cond = cond;
		this.truePart = truePart;
		this.falsePart = falsePart;
	}

	public Expression getCondition() {
		return cond;
	}

	public EmbeddedStatement getTruePart() {
		return truePart;
	}

	public EmbeddedStatement getFalsePart() {
		return falsePart;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
