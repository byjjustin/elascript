package org.elascript.runtime;

import java.util.*;

public class ListQueryable<E> extends Queryable<E> implements List<E> {
	private List<E> internal;

	protected ListQueryable(List<E> internal) {
		super(internal);
		this.internal = internal;
	}

	@Override
	public int size() {
		return internal.size();
	}

	@Override
	public boolean isEmpty() {
		return internal.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return internal.contains(o);
	}

	@Override
	public Object[] toArray() {
		return internal.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return internal.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return internal.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return internal.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return internal.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return internal.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return internal.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return internal.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return internal.retainAll(c);
	}

	@Override
	public void clear() {
		internal.clear();
	}

	@Override
	public E get(int index) {
		return internal.get(index);
	}

	@Override
	public E set(int index, E element) {
		return internal.set(index, element);
	}

	@Override
	public void add(int index, E element) {
		internal.add(index, element);
	}

	@Override
	public E remove(int index) {
		return internal.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return internal.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return internal.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return internal.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return internal.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return internal.subList(fromIndex, toIndex);
	}
}
