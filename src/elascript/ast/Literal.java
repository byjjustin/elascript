package elascript.ast;

import elascript.*;

public class Literal extends Expression {
	private Object value;

	public Literal(Object value, int line) {
		super(line);
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
