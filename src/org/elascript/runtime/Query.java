package org.elascript.runtime;

import java.util.*;

import org.elascript.*;

public final class Query {
	public static <T> Queryable<T> from(Iterable<T> source) {
		CodeContract.requiresArgumentNotNull(source, "source");
		return new Queryable<T>(source);
	}

	public static <T> ListQueryable<T> from(List<T> source) {
		CodeContract.requiresArgumentNotNull(source, "source");
		return new ListQueryable<T>(source);
	}
}
