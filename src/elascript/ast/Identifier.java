package elascript.ast;

import elascript.*;
import elascript.symbol.*;

public class Identifier extends Expression {
	private String name;
	private Symbol symbol;

	public Identifier(String name, int line) {
		super(line);
		CodeContract.requiresArgumentNotNull(name, "name");
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
