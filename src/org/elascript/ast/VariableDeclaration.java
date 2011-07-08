package org.elascript.ast;

import org.elascript.*;

public class VariableDeclaration extends Statement {
	private String name;

	public VariableDeclaration(String name, int line) {
		super(line);
		CodeContract.requiresArgumentNotNull(name, "name");
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
