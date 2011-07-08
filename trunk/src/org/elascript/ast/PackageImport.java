package org.elascript.ast;

import org.elascript.*;

public class PackageImport extends ASTNode {
	private String packageName;

	public PackageImport(String packageName, int line) {
		super(line);
		CodeContract.requiresArgumentNotNull(packageName, "packageName");
		this.packageName = packageName;
	}

	public String getPackageName() {
		return packageName;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		return visitor.visit(this);
	}
}
