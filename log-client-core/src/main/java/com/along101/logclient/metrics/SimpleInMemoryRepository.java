package com.along101.logclient.metrics;

import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class SimpleInMemoryRepository<T> {

	private ConcurrentNavigableMap<String, T> values = new ConcurrentSkipListMap<String, T>();

	private final ConcurrentMap<String, Object> locks = new ConcurrentHashMap<String, Object>();

	public T update(String name, Callback<T> callback) {
		Object lock = getLock(name);
		synchronized (lock) {
			T current = this.values.get(name);
			T value = callback.modify(current);
			this.values.put(name, value);
			return value;
		}
	}

	private Object getLock(String name) {
		Object lock = this.locks.get(name);
		if (lock == null) {
			Object newLock = new Object();
			lock = this.locks.putIfAbsent(name, newLock);
			if (lock == null) {
				lock = newLock;
			}
		}
		return lock;
	}

	public void set(String name, T value) {
		this.values.put(name, value);
	}

	public long count() {
		return this.values.size();
	}

	public void remove(String name) {
		this.values.remove(name);
	}

	public T findOne(String name) {
		return this.values.get(name);
	}

	public Iterable<T> findAll() {
		return new ArrayList<T>(this.values.values());
	}

	public Iterable<T> findAllWithPrefix(String prefix) {
		if (prefix.endsWith(".*")) {
			prefix = prefix.substring(0, prefix.length() - 1);
		}
		if (!prefix.endsWith(".")) {
			prefix = prefix + ".";
		}
		return new ArrayList<T>(
				this.values.subMap(prefix, false, prefix + "~", true).values());
	}

	public void setValues(ConcurrentNavigableMap<String, T> values) {
		this.values = values;
	}

	protected NavigableMap<String, T> getValues() {
		return this.values;
	}

	/**
	 * Callback used to update a value.
	 *
	 * @param <T> the value type
	 */
	public interface Callback<T> {

		/**
		 * Modify an existing value.
		 * @param current the value to modify
		 * @return the updated value
		 */
		T modify(T current);

	}

}
