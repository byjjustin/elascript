package org.elascript.ast;

import org.elascript.*;

public class Continue extends EmbeddedStatement {
	public Continue(int line) {
		super(line);
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
