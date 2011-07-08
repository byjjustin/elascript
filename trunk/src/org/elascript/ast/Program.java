package org.elascript.ast;

import java.util.*;

import org.elascript.*;


public class Program extends ASTNode {
	private List<Statement> statements;
	private List<PackageImport> packageImports;

	public Program(List<Statement> statements,
			List<PackageImport> packageImports) {
		super(1);
		this.statements = Utils.makeReadOnlyCopy(statements);
		this.packageImports = Utils.makeReadOnlyCopy(packageImports);
	}

	public Iterable<Statement> getStatements() {
		return statements;
	}

	public Iterable<PackageImport> getPackageImports() {
		return packageImports;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		return visitor.visit(this);
	}
}
