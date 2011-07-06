package elascript.runtime;

import java.util.*;

import elascript.interpreter.*;

public final class RuntimeHelper {
	public static Iterator<?> getIterator(Object obj)
			throws TypeDismatchException {
		if (obj instanceof Iterable<?>)
			return ((Iterable<?>) obj).iterator();
		if (obj.getClass().isArray())
			return Arrays.asList(obj).iterator();
		throw new TypeDismatchException("array or iterable");
	}
}
