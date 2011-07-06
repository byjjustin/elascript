package elascript.ast;

import java.util.*;

import elascript.*;
import elascript.symbol.*;

public class Function extends Expression {
	private List<String> params;
	private List<Statement> statements;
	private Scope functionScope;

	public Function(List<String> params, List<Statement> statements, int line) {
		super(line);
		this.params = Utils.makeReadOnlyCopy(params);
		this.statements = Utils.makeReadOnlyCopy(statements);
	}

	public Iterable<String> getParameters() {
		return params;
	}

	public Iterable<Statement> getStatements() {
		return statements;
	}

	public void setFunctionScope(Scope functionScope) {
		this.functionScope = functionScope;
	}

	public Scope getFunctionScope() {
		return functionScope;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
