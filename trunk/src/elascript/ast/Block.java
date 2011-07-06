package elascript.ast;

import java.util.*;

import elascript.*;
import elascript.symbol.*;

public class Block extends EmbeddedStatement {
	private List<Statement> statements;
	private Scope blockScope;

	public Block(List<Statement> statements, int line) {
		super(line);
		this.statements = Utils.makeReadOnlyCopy(statements);
	}

	public Iterable<Statement> getStatements() {
		return statements;
	}

	public void setBlockScope(Scope blockScope) {
		this.blockScope = blockScope;
	}

	public Scope getBlockScope() {
		return blockScope;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
