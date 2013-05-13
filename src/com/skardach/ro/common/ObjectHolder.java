package com.skardach.ro.common;

/**
 * Simple wrapper class to hold an object to be passed as an in/out parameter
 * of a method.
 * @author Stanislaw Kardach
 *
 * @param <T> Type of the value to be held inside wrapper.
 */
public final class ObjectHolder<T> {
	T _obj;
	/**
	 * Create the wrapper over a given object
	 * @param iObject Object to be wrapped
	 */
	public ObjectHolder(T iObject) {
		_obj = iObject;
	}
	/**
	 * Get wrapped object
	 * @return reference to the wrapped object
	 */
	public T getObject() {
		return _obj;
	}
	/**
	 * Set the new wrapped object
	 * @param iObject new object to wrap
	 */
	public void setObject(T iObject) {
		_obj = iObject;
	}
}
