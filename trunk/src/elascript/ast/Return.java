package elascript.ast;

import elascript.*;

public class Return extends EmbeddedStatement {
	private Expression returnValue;

	public Return(Expression returnValue, int line) {
		super(line);
		this.returnValue = returnValue;
	}

	public Expression getReturnValue() {
		return returnValue;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
