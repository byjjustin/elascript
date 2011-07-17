package org.elascript.runtime;

import java.util.*;

import org.elascript.*;

public class Queryable<E> implements Iterable<E> {
	private Iterable<E> internal;

	protected Queryable(Iterable<E> internal) {
		this.internal = internal;
	}

	public Queryable<E> where(Delegate pred) {
		CodeContract.requiresArgumentNotNull(pred, "pred");
		List<E> result = new ArrayList<E>();
		try {
			for (E elem : internal)
				if ((Boolean) pred.execute(elem))
					result.add(elem);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new Queryable<E>(result);
	}

	public Queryable<Object> forEach(Delegate selector) {
		CodeContract.requiresArgumentNotNull(selector, "selector");
		List<Object> result = new ArrayList<Object>();
		try {
			for (E elem : internal)
				result.add(selector.execute(elem));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new Queryable<Object>(result);
	}

	public Queryable<E> filter(Delegate pred) {
		CodeContract.requiresArgumentNotNull(pred, "pred");
		Iterator<E> iter = internal.iterator();
		try {
			while (iter.hasNext()) {
				E elem = iter.next();
				if (!(Boolean) pred.execute(elem))
					iter.remove();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public static <T> Queryable<T> from(Iterable<T> source) {
		CodeContract.requiresArgumentNotNull(source, "source");
		return new Queryable<T>(source);
	}

	@Override
	public Iterator<E> iterator() {
		return internal.iterator();
	}
}
