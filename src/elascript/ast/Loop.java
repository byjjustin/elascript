package elascript.ast;

import elascript.*;

public class Loop extends EmbeddedStatement {
	private Expression cond;
	private EmbeddedStatement inc;
	private EmbeddedStatement body;

	public Loop(Expression cond, EmbeddedStatement inc, EmbeddedStatement body,
			int line) {
		super(line);
		this.cond = cond;
		this.inc = inc;
		this.body = body;
	}

	public Expression getCondition() {
		return cond;
	}

	public EmbeddedStatement getIncrement() {
		return inc;
	}

	public EmbeddedStatement getBody() {
		return body;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
