package org.elascript.interpreter;

import java.lang.reflect.*;
import java.util.*;

import org.elascript.*;
import org.elascript.ast.*;
import org.elascript.runtime.*;
import org.elascript.symbol.*;

public class ESInterpreter extends ASTVisitor<Result, InterpretingException>
		implements Interpreter {
	private MemorySpace global;
	private MemorySpace current;
	private SecurityChecker checker;

	public ESInterpreter() {
	}

	public ESInterpreter(SecurityChecker checker) {
		this.checker = checker;
	}

	@Override
	public void execute(Program ast) throws InterpretingException {
		CodeContract.requiresArgumentNotNull(ast, "ast");
		visit(ast);
	}

	@Override
	public Object execute(Function ast, MemorySpace env, Object[] args)
			throws FunctionCallException, InterpretingException {
		CodeContract.requiresArgumentNotNull(ast, "ast");
		CodeContract.requiresArgumentNotNull(env, "env");
		if (args == null)
			args = new Object[0];

		MemorySpace envBackup = current;
		current = new MemorySpace(ast.getFunctionScope(), env, this);
		Iterator<String> paramIt = ast.getParameters().iterator();
		for (int i = 0; i < args.length; ++i)
			if (paramIt.hasNext())
				current.put(paramIt.next(), args[i]);
			else
				throw new FunctionCallException("Function siguature dismatch.");
		if (paramIt.hasNext())
			throw new FunctionCallException("Function siguature dismatch.");

		try {
			visitSeqStatements(ast.getStatements());
			return null;
		} catch (ReturnException e) {
			return e.getReturnValue();
		} finally {
			current = envBackup;
		}
	}

	@Override
	protected Result visit(ASTNode ast) throws InterpretingException {
		Result res = super.visit(ast);
		if (checker != null)
			try {
				if (!checker.check(res))
					throw new ScriptSecurityException(ast.getLine());
			} catch (AccessException e) {
				throw new InterpretingException(ast.getLine(), e.getMessage());
			}
		return res;
	}

	@Override
	protected Result visit(Binary ast) throws InterpretingException {
		try {
			return OperatorHelper.binary(ast, this);
		} catch (ArithmeticException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		} catch (TypeDismatchException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		} catch (AccessException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		}
	}

	@Override
	protected Result visit(Block ast) throws InterpretingException {
		current = new MemorySpace(ast.getBlockScope(), current, this);
		visitSeqStatements(ast.getStatements());
		current = current.getParent();
		return VoidResult.INSTANCE;
	}

	@Override
	protected Result visit(Break ast) throws InterpretingException {
		throw new BreakException(ast.getLine(),
				"Cannot find loop statement outside.");
	}

	@Override
	protected Result visit(Call ast) throws InterpretingException {
		try {
			Object function = visit(ast.getFunction()).asValue();
			Object[] args = visitArgs(ast.getArguments());
			if (!(function instanceof Delegate))
				throw new TypeDismatchException("delegate");
			return new RValueResult(((Delegate) function).execute(args));
		} catch (TypeDismatchException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		} catch (FunctionCallException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		} catch (AccessException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		}
	}

	@Override
	protected Result visit(Continue ast) throws InterpretingException {
		throw new ContinueException(ast.getLine(),
				"Cannot find loop statement outside.");
	}

	@Override
	protected Result visit(Evaluation ast) throws InterpretingException {
		visit(ast.getExpression());
		return VoidResult.INSTANCE;
	}

	@Override
	protected Result visit(Function ast) throws InterpretingException {
		return new RValueResult(new InternalDelegate(ast, current));
	}

	@Override
	protected Result visit(Identifier ast) throws InterpretingException {
		Symbol sym = ast.getSymbol();
		if (sym instanceof VariableSymbol)
			return new VariableResult((VariableSymbol) sym, current);
		if (sym instanceof TypeSymbol)
			return new TypeResult(((TypeSymbol) sym).getType());
		throw new UnsupportedOperationException("Unsupported identifier.");
	}

	@Override
	protected Result visit(IfElse ast) throws InterpretingException {
		try {
			if (visit(ast.getCondition()).asBoolean())
				visit(ast.getTruePart());
			else
				visit(ast.getFalsePart());
		} catch (TypeDismatchException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		} catch (AccessException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		}
		return VoidResult.INSTANCE;
	}

	@Override
	protected Result visit(InstanceOf ast) throws InterpretingException {
		try {
			Object left = visit(ast.getLeft()).asValue();
			Class<?> type = visit(ast.getType()).asType();
			return new RValueResult(type.isInstance(left));
		} catch (TypeDismatchException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		} catch (AccessException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		}
	}

	@Override
	protected Result visit(Literal ast) throws InterpretingException {
		return new RValueResult(ast.getValue());
	}

	@Override
	protected Result visit(Loop ast) throws InterpretingException {
		try {
			while (ast.getCondition() == null
					|| visit(ast.getCondition()).asBoolean()) {
				try {
					visit(ast.getBody());
				} catch (ContinueException e) {
				}
				visit(ast.getIncrement());
			}
		} catch (BreakException e) {
		} catch (TypeDismatchException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		} catch (AccessException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		}
		return VoidResult.INSTANCE;
	}

	@Override
	protected Result visit(MemberOf ast) throws InterpretingException {
		Result left = visit(ast.getLeft());
		Object instance = null;
		Class<?> type;
		try {
			if (left instanceof TypeResult)
				type = ((TypeResult) left).getType();
			else if (left instanceof ValueResult) {
				instance = ((ValueResult) left).getValue();
				type = instance.getClass();
			} else
				throw new InterpretingException(ast.getLine(),
						"Type name or object value expected.");
			for (Method method : type.getMethods())
				if (method.getName().equals(ast.getMemberName()))
					return new RValueResult(new ExternalDelegate(type,
							ast.getMemberName(), instance));

			Field field = type.getField(ast.getMemberName());
			if (Modifier.isFinal(field.getModifiers()))
				return new RValueResult(field.get(instance));
			return new FieldResult(field, instance);
		} catch (NoSuchFieldException e) {
			throw new InterpretingException(ast.getLine(), ast.getMemberName()
					+ " is not a member of the given type.");
		} catch (Exception e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		}
	}

	@Override
	protected Result visit(NewArray ast) throws InterpretingException {
		try {
			Class<?> type = visit(ast.getType()).asType();
			Object[] bounds = visitArgs(ast.getBounds());
			int[] boundsInt = new int[bounds.length];
			for (int i = 0; i < bounds.length; ++i) {
				Number number = TypeHelper.tryToNumber(bounds[i]);
				if (number == null)
					throw new InterpretingException(ast.getLine(),
							"Array bound must be integer.");
				boundsInt[i] = number.intValue();
			}
			return new RValueResult(Array.newInstance(type, boundsInt));
		} catch (NegativeArraySizeException e) {
			throw new InterpretingException(ast.getLine(),
					"Array length cannot be negative.");
		} catch (Exception e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		}
	}

	@Override
	protected Result visit(NewObject ast) throws InterpretingException {
		try {
			Class<?> type = visit(ast.getType()).asType();
			Object[] args = visitArgs(ast.getArguments());
			return new RValueResult(TypeHelper.matchConstructor(type, args)
					.newInstance(args));
		} catch (Exception e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		}
	}

	@Override
	protected Result visit(PackageImport ast) throws InterpretingException {
		return VoidResult.INSTANCE;
	}

	@Override
	protected Result visit(Program ast) throws InterpretingException {
		global = new MemorySpace(ast.getScope(), this);
		current = global;
		visitSeqStatements(ast.getStatements());
		return VoidResult.INSTANCE;
	}

	@Override
	protected Result visit(Return ast) throws InterpretingException {
		try {
			throw new ReturnException(visit(ast.getReturnValue()).asValue(),
					ast.getLine(),
					"The return statement has no function outside.");
		} catch (TypeDismatchException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		} catch (AccessException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		}
	}

	@Override
	protected Result visit(Unary ast) throws InterpretingException {
		try {
			return OperatorHelper.unary(ast, this);
		} catch (TypeDismatchException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		} catch (AccessException e) {
			throw new InterpretingException(ast.getLine(), e.getMessage());
		}
	}

	@Override
	protected Result visit(VariableDeclaration ast)
			throws InterpretingException {
		return VoidResult.INSTANCE;
	}

	private void visitSeqStatements(Iterable<Statement> iterable)
			throws InterpretingException {
		for (Statement statement : iterable) {
			visit(statement);
		}
	}

	private Object[] visitArgs(Iterable<Expression> exprs)
			throws TypeDismatchException, InterpretingException,
			AccessException {
		List<Object> args = new ArrayList<Object>();
		for (Expression expr : exprs)
			args.add(visit(expr).asValue());
		return args.toArray();
	}

	@Override
	public MemorySpace getGlobalEnvironment() {
		return global;
	}
}
