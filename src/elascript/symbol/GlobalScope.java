package elascript.symbol;

import java.util.*;
import elascript.*;

public class GlobalScope extends LocalScope {
	List<String> packageImports = new ArrayList<String>();

	public static final String DEFAULT_ELASCRIPT_PACKAGE = "elascript.runtime";
	public static final String DEFAULT_JAVA_PACKAGE = "java.lang";

	public GlobalScope() {
		super(null);
		packageImports.add(DEFAULT_ELASCRIPT_PACKAGE);
		packageImports.add(DEFAULT_JAVA_PACKAGE);
	}

	public void definePackageImport(String packageName) {
		CodeContract.requiresArgumentNotNull(packageName, "packageName");
		if (!packageImports.contains(packageName))
			packageImports.add(packageName);
	}

	@Override
	public Symbol resolve(String name) throws SymbolException {
		Symbol sym = super.resolve(name);
		if (sym != null)
			return sym;

		boolean found = false;
		Class<?> type = null;
		for (String packageImport : packageImports)
			try {
				type = Class.forName(packageImport + "." + name);
				if (found)
					throw new SymbolException("Ambiguous Java type name "
							+ name + ".");
				else
					found = true;
			} catch (ClassNotFoundException e) {
			}
		if (!found)
			return null;
		sym = new TypeSymbol(type);
		define(sym);
		return sym;
	}
}
