package org.elascript.ast;

public abstract class ASTVisitor<T, E extends Throwable> {
	protected T visit(ASTNode ast) throws E {
		if (ast != null)
			return ast.accept(this);
		return null;
	}

	protected abstract T visit(Binary ast) throws E;

	protected abstract T visit(Block ast) throws E;

	protected abstract T visit(Break ast) throws E;

	protected abstract T visit(Call ast) throws E;

	protected abstract T visit(Continue ast) throws E;

	protected abstract T visit(Evaluation ast) throws E;

	protected abstract T visit(Function ast) throws E;

	protected abstract T visit(Identifier ast) throws E;

	protected abstract T visit(IfElse ast) throws E;

	protected abstract T visit(InstanceOf ast) throws E;

	protected abstract T visit(Literal ast) throws E;

	protected abstract T visit(Loop ast) throws E;

	protected abstract T visit(MemberOf ast) throws E;

	protected abstract T visit(NewArray ast) throws E;

	protected abstract T visit(NewObject ast) throws E;

	protected abstract T visit(PackageImport ast) throws E;

	protected abstract T visit(Program ast) throws E;

	protected abstract T visit(Return ast) throws E;

	protected abstract T visit(Unary ast) throws E;

	protected abstract T visit(VariableDeclaration ast) throws E;
}
