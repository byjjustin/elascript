package elascript.symbol;

import elascript.*;
import elascript.ast.*;

public class ESSymbolResolver extends ASTVisitor<ASTNode, ParsingException>
		implements SymbolResolver {
	private GlobalScope global;

	@Override
	public void resolve(Program ast) throws ParsingException {
		CodeContract.requiresArgumentNotNull(ast, "ast");
		visit(ast);
	}

	@Override
	protected ASTNode visit(Binary ast) throws ParsingException {
		ast.getLeft().setScope(ast.getScope());
		ast.getRight().setScope(ast.getScope());
		visit(ast.getLeft());
		visit(ast.getRight());
		return ast;
	}

	@Override
	protected ASTNode visit(Block ast) throws ParsingException {
		Scope blockScope = new LocalScope(ast.getScope());
		ast.setBlockScope(blockScope);
		visitSeqNodes(ast.getStatements(), blockScope);
		return ast;
	}

	@Override
	protected ASTNode visit(Break ast) throws ParsingException {
		return ast;
	}

	@Override
	protected ASTNode visit(Call ast) throws ParsingException {
		ast.getFunction().setScope(ast.getScope());
		visit(ast.getFunction());
		visitSeqNodes(ast.getArguments(), ast.getScope());
		return ast;
	}

	@Override
	protected ASTNode visit(Continue ast) throws ParsingException {
		return ast;
	}

	@Override
	protected ASTNode visit(Evaluation ast) throws ParsingException {
		ast.getExpression().setScope(ast.getScope());
		visit(ast.getExpression());
		return ast;
	}

	@Override
	protected ASTNode visit(Function ast) throws ParsingException {
		Scope funcScope = new LocalScope(ast.getScope());
		ast.setFunctionScope(funcScope);
		try {
			for (String param : ast.getParameters())
				funcScope.define(new VariableSymbol(param));
		} catch (SymbolException e) {
			throw new ParsingException(ast.getLine(), e.getMessage());
		}
		visitSeqNodes(ast.getStatements(), funcScope);
		return ast;
	}

	@Override
	protected ASTNode visit(Identifier ast) throws ParsingException {
		Symbol sym;
		try {
			sym = ast.getScope().resolve(ast.getName());
		} catch (SymbolException e) {
			throw new ParsingException(ast.getLine(), e.getMessage());
		}
		if (sym == null)
			throw new ParsingException(ast.getLine(), "The identifier "
					+ ast.getName() + " cannot be resolved.");
		ast.setSymbol(sym);
		return ast;
	}

	@Override
	protected ASTNode visit(IfElse ast) throws ParsingException {
		ast.getCondition().setScope(ast.getScope());
		visit(ast.getCondition());
		if (ast.getTruePart() != null) {
			ast.getTruePart().setScope(ast.getScope());
			visit(ast.getTruePart());
		}
		if (ast.getFalsePart() != null) {
			ast.getFalsePart().setScope(ast.getScope());
			visit(ast.getFalsePart());
		}
		return ast;
	}

	@Override
	protected ASTNode visit(InstanceOf ast) throws ParsingException {
		ast.getLeft().setScope(ast.getScope());
		ast.getType().setScope(ast.getScope());
		visit(ast.getLeft());
		visit(ast.getType());
		return ast;
	}

	@Override
	protected ASTNode visit(Literal ast) throws ParsingException {
		return ast;
	}

	@Override
	protected ASTNode visit(Loop ast) throws ParsingException {
		if (ast.getCondition() != null) {
			ast.getCondition().setScope(ast.getScope());
			visit(ast.getCondition());
		}
		if (ast.getIncrement() != null) {
			ast.getIncrement().setScope(ast.getScope());
			visit(ast.getIncrement());
		}
		if (ast.getBody() != null) {
			ast.getBody().setScope(ast.getScope());
			visit(ast.getBody());
		}
		return ast;
	}

	@Override
	protected ASTNode visit(MemberOf ast) throws ParsingException {
		ast.getLeft().setScope(ast.getScope());
		visit(ast.getLeft());
		return ast;
	}

	@Override
	protected ASTNode visit(NewArray ast) throws ParsingException {
		ast.getType().setScope(ast.getScope());
		visit(ast.getType());
		visitSeqNodes(ast.getBounds(), ast.getScope());
		return ast;
	}

	@Override
	protected ASTNode visit(NewObject ast) throws ParsingException {
		ast.getType().setScope(ast.getScope());
		visit(ast.getType());
		visitSeqNodes(ast.getArguments(), ast.getScope());
		return ast;
	}

	@Override
	protected ASTNode visit(PackageImport ast) throws ParsingException {
		global.definePackageImport(ast.getPackageName());
		return ast;
	}

	@Override
	protected ASTNode visit(Program ast) throws ParsingException {
		global = new GlobalScope();
		ast.setScope(global);
		visitSeqNodes(ast.getPackageImports(), global);
		visitSeqNodes(ast.getStatements(), global);
		return ast;
	}

	@Override
	protected ASTNode visit(Return ast) throws ParsingException {
		if (ast.getReturnValue() != null) {
			ast.getReturnValue().setScope(ast.getScope());
			visit(ast.getReturnValue());
		}
		return ast;
	}

	@Override
	protected ASTNode visit(Unary ast) throws ParsingException {
		ast.getOperand().setScope(ast.getScope());
		visit(ast.getOperand());
		return ast;
	}

	@Override
	protected ASTNode visit(VariableDeclaration ast) throws ParsingException {
		try {
			ast.getScope().define(new VariableSymbol(ast.getName()));
		} catch (SymbolException e) {
			throw new ParsingException(ast.getLine(), e.getMessage());
		}
		return ast;
	}

	private <T extends ASTNode> void visitSeqNodes(Iterable<T> nodes,
			Scope scope) throws ParsingException {
		for (T node : nodes) {
			node.setScope(scope);
			visit(node);
		}
	}
}
