package elascript;

import elascript.ast.*;
import elascript.interpreter.*;
import elascript.parser.*;
import elascript.symbol.*;

public class ScriptEngine {
	private SourceReader source;
	private Lexer lexer;
	private Parser parser;
	private SymbolResolver resolver;
	private Interpreter interpreter;
	private SecurityChecker checker;
	private Program ast;

	public ScriptEngine(String source) {
		CodeContract.requiresArgumentNotNull(source, "source");
		this.source = new StringSourceReader(source);
	}

	public ScriptEngine(Program ast) {
		CodeContract.requiresArgumentNotNull(ast, "ast");
		this.ast = ast;
	}

	public void compile() throws ParsingException {
		if (ast != null)
			return;
		lexer = new ESLexer(source);
		parser = new ESParser(lexer);
		ast = parser.parse();
		resolver = new ESSymbolResolver();
		resolver.resolve(ast);
	}

	public void execute() throws ParsingException, InterpretingException {
		if (ast == null)
			compile();
		interpreter = new ESInterpreter(checker);
		interpreter.execute(ast);
	}

	public SecurityChecker getSecurityChecker() {
		return checker;
	}

	public void setSecurityChecker(SecurityChecker checker) {
		this.checker = checker;
	}

	public Interpreter getInterpreter() {
		return interpreter;
	}

	public Program getAbstractSyntaxTree() {
		return ast;
	}
}
