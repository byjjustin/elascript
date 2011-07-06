package elascript.parser;

import java.util.*;

import elascript.*;
import elascript.ast.*;

public class ESParser extends LLParser {
	public ESParser(Lexer lexer) throws ParsingException {
		super(lexer);
	}

	@Override
	public Program parse() throws ParsingException {
		List<PackageImport> packageImports = new ArrayList<PackageImport>();
		while (peek("using"))
			packageImports.add(usingStatement());
		return new Program(seqStatements(), packageImports);
	}

	private List<Statement> seqStatements() throws ParsingException {
		List<Statement> list = new ArrayList<Statement>();
		while (!peek("}") && !peek(EOFToken.EOF))
			statement(list);
		return list;
	}

	private PackageImport usingStatement() throws ParsingException {
		int line = peekLine();
		match("using");
		StringBuffer packageName = new StringBuffer();
		packageName.append(matchIdentifier().getName());
		while (tryMatch("."))
			packageName.append('.').append(matchIdentifier().getName());
		match(";");
		return new PackageImport(packageName.toString(), line);
	}

	private void statement(List<Statement> list) throws ParsingException {
		if (peek("var"))
			varStatement(list);
		else if (peek("function"))
			functionStatement(list);
		else {
			Statement statement = embeddedStatement();
			if (statement != null)
				list.add(statement);
		}
	}

	private EmbeddedStatement embeddedStatement() throws ParsingException {
		int line = peekLine();
		if (tryMatch("{")) {
			EmbeddedStatement block = combine(seqStatements(), line);
			match("}");
			return block;
		}
		if (peek("if"))
			return ifStatement();
		if (peek("for"))
			return forStatement();
		if (peek("foreach"))
			return foreachStatement();
		if (peek("while"))
			return whileStatement();
		if (peek("do"))
			return doStatement();
		if (peek("break"))
			return breakStatement();
		if (peek("continue"))
			return continueStatement();
		if (peek("return"))
			return returnStatement();
		if (tryMatch(";"))
			return null;
		return evaluationStatement();
	}

	private void varStatement(List<Statement> list) throws ParsingException {
		match("var");
		variableDeclarator(list);
		while (tryMatch(","))
			variableDeclarator(list);
		match(";");
	}

	private void variableDeclarator(List<Statement> list)
			throws ParsingException {
		int line = peekLine();
		Identifier id = matchIdentifier();
		list.add(new VariableDeclaration(id.getName(), line));
		if (tryMatch("="))
			list.add(new Evaluation(new Binary(id, expr(),
					BinaryOperator.ASSIGN, line), line));
	}

	private void functionStatement(List<Statement> list)
			throws ParsingException {
		int line = peekLine();
		match("function");
		Identifier id = matchIdentifier();
		list.add(new VariableDeclaration(id.getName(), line));
		list.add(new Evaluation(new Binary(id, functionSignatureAndBody(),
				BinaryOperator.ASSIGN, line), line));
	}

	private List<String> functionSignature() throws ParsingException {
		List<String> params = new ArrayList<String>();
		match("(");
		if (!peek(")")) {
			params.add(matchIdentifier().getName());
			while (tryMatch(","))
				params.add(matchIdentifier().getName());
		}
		match(")");
		return params;
	}

	private Function functionSignatureAndBody() throws ParsingException {
		int line = peekLine();
		List<String> params = functionSignature();
		match("{");
		Function ast = new Function(params, seqStatements(), line);
		match("}");
		return ast;
	}

	private IfElse ifStatement() throws ParsingException {
		int line = peekLine();
		match("if").match("(");
		Expression cond = expr();
		match(")");
		EmbeddedStatement truePart = embeddedStatement();
		EmbeddedStatement falsePart = null;
		if (tryMatch("else"))
			falsePart = embeddedStatement();
		return new IfElse(cond, truePart, falsePart, line);
	}

	private EmbeddedStatement forStatement() throws ParsingException {
		int line = peekLine();
		List<Statement> list = new ArrayList<Statement>();
		match("for").match("(");
		if (peek("var"))
			varStatement(list);
		else if (!peek(";")) {
			expressionList(list);
			match(";");
		}
		Expression cond = null;
		List<Statement> inc = new ArrayList<Statement>();
		if (!peek(";"))
			cond = expr();
		match(";");
		if (!peek(")"))
			expressionList(inc);
		match(")");
		list.add(new Loop(cond, combine(inc, line), embeddedStatement(), line));
		return combine(list, line);
	}

	private void expressionList(List<Statement> list) throws ParsingException {
		int line = peekLine();
		list.add(new Evaluation(expr(), line));
		while (tryMatch(",")) {
			line = peekLine();
			list.add(new Evaluation(expr(), line));
		}
	}

	private EmbeddedStatement foreachStatement() throws ParsingException {
		int line = peekLine();
		List<Statement> list = new ArrayList<Statement>();
		match("foreach").match("(").match("var");
		Identifier id = matchIdentifier();
		Identifier iterator = new Identifier("__iterator", line);
		list.add(new VariableDeclaration(id.getName(), line));
		list.add(new VariableDeclaration(iterator.getName(), line));
		match(":");
		Expression set = expr();
		match(")");
		List<Expression> args = new ArrayList<Expression>();
		args.add(set);
		list.add(new Evaluation(new Binary(iterator, new Call(new MemberOf(
				new Identifier("RuntimeHelper", line), "getIterator", line),
				args, line), BinaryOperator.ASSIGN, line), line));
		Expression cond = new Call(new MemberOf(iterator, "hasNext", line),
				null, line);
		List<Statement> body = new ArrayList<Statement>();
		body.add(new Evaluation(new Binary(id, new Call(new MemberOf(iterator,
				"next", line), null, line), BinaryOperator.ASSIGN, line), line));
		body.add(embeddedStatement());
		list.add(new Loop(cond, null, combine(body, line), line));
		return combine(list, line);
	}

	private Loop whileStatement() throws ParsingException {
		int line = peekLine();
		match("while").match("(");
		Expression cond = expr();
		match(")");
		return new Loop(cond, null, embeddedStatement(), line);
	}

	private EmbeddedStatement doStatement() throws ParsingException {
		int line = peekLine();
		List<Statement> list = new ArrayList<Statement>();
		Identifier check = new Identifier("__check", line);
		list.add(new VariableDeclaration(check.getName(), line));
		list.add(new Evaluation(new Binary(check, new Literal(true, line),
				BinaryOperator.ASSIGN, line), line));
		EmbeddedStatement body = embeddedStatement();
		match("while").match("(");
		EmbeddedStatement inc = new Evaluation(new Binary(check, expr(),
				BinaryOperator.ASSIGN, line), line);
		match(")").match(";");
		list.add(new Loop(check, inc, body, line));
		return combine(list, line);
	}

	private Break breakStatement() throws ParsingException {
		int line = peekLine();
		match("break").match(";");
		return new Break(line);
	}

	private Continue continueStatement() throws ParsingException {
		int line = peekLine();
		match("continue").match(";");
		return new Continue(line);
	}

	private Return returnStatement() throws ParsingException {
		int line = peekLine();
		match("return");
		Expression returnValue = null;
		if (!peek(";"))
			returnValue = expr();
		match(";");
		return new Return(returnValue, line);
	}

	private Evaluation evaluationStatement() throws ParsingException {
		int line = peekLine();
		Expression expr = expr();
		match(";");
		return new Evaluation(expr, line);
	}

	private Expression primary() throws ParsingException {
		int line = peekLine();
		Expression primary;
		if (tryMatch("(")) {
			primary = expr();
			match(")");
		} else {
			Token token = consume();
			if (token.getValue().equals("new")) {
				Identifier type = matchIdentifier();
				if (peek("(")) {
					List<Expression> args = argList();
					primary = new NewObject(type, args, line);
				} else if (peek("[")) {
					List<Expression> bounds = new ArrayList<Expression>();
					do
						bounds.add(indexer());
					while (peek("["));
					primary = new NewArray(type, bounds, line);
				} else
					throw new ParsingException(line, "( or [ expected.");
			} else if (token.getValue().equals("function"))
				primary = functionSignatureAndBody();
			else if (token instanceof IdentifierToken)
				primary = new Identifier(token.getValue(), line);
			else if (token instanceof LiteralToken)
				primary = new Literal(((LiteralToken) token).getLiteral(), line);
			else
				throw new ParsingException(line,
						"Identifier or literal expected.");
		}

		while (true) {
			line = peekLine();
			if (tryMatch("."))
				primary = new MemberOf(primary, matchIdentifier().getName(),
						line);
			else if (peek("["))
				primary = new Binary(primary, indexer(),
						BinaryOperator.ARRAY_INDEX, line);
			else if (peek("("))
				primary = new Call(primary, argList(), line);
			else if (tryMatch("++"))
				primary = new Unary(primary, UnaryOperator.POST_INC, line);
			else if (tryMatch("--"))
				primary = new Unary(primary, UnaryOperator.POST_DEC, line);
			else
				break;
		}
		return primary;
	}

	private List<Expression> argList() throws ParsingException {
		List<Expression> args = new ArrayList<Expression>();
		match("(");
		if (!peek(")")) {
			args.add(expr());
			while (tryMatch(","))
				args.add(expr());
		}
		match(")");
		return args;
	}

	private Expression indexer() throws ParsingException {
		match("[");
		Expression expr = expr();
		match("]");
		return expr;
	}

	private Expression unary() throws ParsingException {
		int line = peekLine();
		if (tryMatch("+"))
			return new Unary(unary(), UnaryOperator.PLUS, line);
		if (tryMatch("-"))
			return new Unary(unary(), UnaryOperator.MINUS, line);
		if (tryMatch("!"))
			return new Unary(unary(), UnaryOperator.NOT, line);
		if (tryMatch("~"))
			return new Unary(unary(), UnaryOperator.BITWISE_COMPLEMENT, line);
		if (tryMatch("++"))
			return new Unary(unary(), UnaryOperator.PRE_INC, line);
		if (tryMatch("--"))
			return new Unary(unary(), UnaryOperator.PRE_DEC, line);
		return primary();
	}

	private Expression multiplicative() throws ParsingException {
		Expression multiplicative = unary();
		while (true) {
			int line = peekLine();
			if (tryMatch("*"))
				multiplicative = new Binary(multiplicative, unary(),
						BinaryOperator.MULTIPLY, line);
			else if (tryMatch("/"))
				multiplicative = new Binary(multiplicative, unary(),
						BinaryOperator.DIVIDE, line);
			else if (tryMatch("%"))
				multiplicative = new Binary(multiplicative, unary(),
						BinaryOperator.MODULO, line);
			else
				break;
		}
		return multiplicative;
	}

	private Expression additive() throws ParsingException {
		Expression additive = multiplicative();
		while (true) {
			int line = peekLine();
			if (tryMatch("+"))
				additive = new Binary(additive, multiplicative(),
						BinaryOperator.ADD, line);
			else if (tryMatch("-"))
				additive = new Binary(additive, multiplicative(),
						BinaryOperator.SUBTRACT, line);
			else
				break;
		}
		return additive;
	}

	private Expression shift() throws ParsingException {
		Expression shift = additive();
		while (true) {
			int line = peekLine();
			if (tryMatch("<<"))
				shift = new Binary(shift, additive(),
						BinaryOperator.LEFT_SHIFT, line);
			else if (tryMatch(">>"))
				shift = new Binary(shift, additive(),
						BinaryOperator.RIGHT_SHIFT, line);
			else if (tryMatch(">>>"))
				shift = new Binary(shift, additive(),
						BinaryOperator.UNSIGNED_RIGHT_SHIFT, line);
			else
				break;
		}
		return shift;
	}

	private Expression relational() throws ParsingException {
		Expression relational = shift();
		while (true) {
			int line = peekLine();
			if (tryMatch("<"))
				relational = new Binary(relational, shift(),
						BinaryOperator.LESS, line);
			else if (tryMatch(">"))
				relational = new Binary(relational, shift(),
						BinaryOperator.GREATER, line);
			else if (tryMatch("<="))
				relational = new Binary(relational, shift(),
						BinaryOperator.LESS_OR_EQUAL, line);
			else if (tryMatch(">="))
				relational = new Binary(relational, shift(),
						BinaryOperator.GREATER_OR_EQUAL, line);
			else if (tryMatch("is"))
				relational = new InstanceOf(relational, matchIdentifier(), line);
			else
				break;
		}
		return relational;
	}

	private Expression equality() throws ParsingException {
		Expression equality = relational();
		while (true) {
			int line = peekLine();
			if (tryMatch("=="))
				equality = new Binary(equality, relational(),
						BinaryOperator.EQUAL, line);
			else if (tryMatch("!="))
				equality = new Binary(equality, relational(),
						BinaryOperator.NOT_EQUAL, line);
			else
				break;
		}
		return equality;
	}

	private Expression bitwiseAnd() throws ParsingException {
		Expression bitwiseAnd = equality();
		while (true) {
			int line = peekLine();
			if (tryMatch("&"))
				bitwiseAnd = new Binary(bitwiseAnd, equality(),
						BinaryOperator.BITWISE_AND, line);
			else
				break;
		}
		return bitwiseAnd;
	}

	private Expression exclusiveOr() throws ParsingException {
		Expression exclusiveOr = bitwiseAnd();
		while (true) {
			int line = peekLine();
			if (tryMatch("^"))
				exclusiveOr = new Binary(exclusiveOr, bitwiseAnd(),
						BinaryOperator.EXCLUSIVE_OR, line);
			else
				break;
		}
		return exclusiveOr;
	}

	private Expression bitwiseOr() throws ParsingException {
		Expression bitwiseOr = exclusiveOr();
		while (true) {
			int line = peekLine();
			if (tryMatch("|"))
				bitwiseOr = new Binary(bitwiseOr, exclusiveOr(),
						BinaryOperator.BITWISE_OR, line);
			else
				break;
		}
		return bitwiseOr;
	}

	private Expression and() throws ParsingException {
		Expression and = bitwiseOr();
		while (true) {
			int line = peekLine();
			if (tryMatch("&&"))
				and = new Binary(and, bitwiseOr(), BinaryOperator.AND, line);
			else
				break;
		}
		return and;
	}

	private Expression or() throws ParsingException {
		Expression or = and();
		while (true) {
			int line = peekLine();
			if (tryMatch("||"))
				or = new Binary(or, and(), BinaryOperator.OR, line);
			else
				break;
		}
		return or;
	}

	private Expression assignment() throws ParsingException {
		Expression left = or();
		int line = peekLine();
		if (tryMatch("="))
			return new Binary(left, or(), BinaryOperator.ASSIGN, line);
		if (tryMatch("+="))
			return new Binary(left, or(), BinaryOperator.ADD_ASSIGN, line);
		if (tryMatch("-="))
			return new Binary(left, or(), BinaryOperator.SUBTRACT_ASSIGN, line);
		if (tryMatch("*="))
			return new Binary(left, or(), BinaryOperator.MULTIPLY_ASSIGN, line);
		if (tryMatch("/="))
			return new Binary(left, or(), BinaryOperator.DIVIDE_ASSIGN, line);
		if (tryMatch("%="))
			return new Binary(left, or(), BinaryOperator.MODULO_ASSIGN, line);
		if (tryMatch("<<="))
			return new Binary(left, or(), BinaryOperator.LEFT_SHIFT_ASSIGN,
					line);
		if (tryMatch(">>="))
			return new Binary(left, or(), BinaryOperator.RIGHT_SHIFT_ASSIGN,
					line);
		if (tryMatch(">>>="))
			return new Binary(left, or(),
					BinaryOperator.UNSIGNED_RIGHT_SHIFT_ASSIGN, line);
		if (tryMatch("&="))
			return new Binary(left, or(), BinaryOperator.BITWISE_AND_ASSIGN,
					line);
		if (tryMatch("|="))
			return new Binary(left, or(), BinaryOperator.BITWISE_OR_ASSIGN,
					line);
		if (tryMatch("^="))
			return new Binary(left, or(), BinaryOperator.EXCLUSIVE_OR_ASSIGN,
					line);
		return left;
	}

	private Expression expr() throws ParsingException {
		if (tryMatch("$")) {
			int line = peekLine();
			List<String> args;
			if (peek("("))
				args = functionSignature();
			else {
				args = new ArrayList<String>();
				args.add(matchIdentifier().getName());
			}
			match("=>");
			Expression body = expr();
			List<Statement> statements = new ArrayList<Statement>();
			statements.add(new Return(body, line));
			return new Function(args, statements, line);
		}
		return assignment();
	}

	private EmbeddedStatement combine(List<Statement> list, int line) {
		if (list == null || list.isEmpty())
			return null;
		if (list.size() == 1 && list.get(0) instanceof EmbeddedStatement)
			return (EmbeddedStatement) list.get(0);
		return new Block(list, line);
	}

	private Identifier matchIdentifier() throws ParsingException {
		int line = peekLine();
		Token token = consume();
		if (token instanceof IdentifierToken)
			return new Identifier(token.getValue(), line);
		throw new ParsingException(line, "Identifier expected.");
	}
}
