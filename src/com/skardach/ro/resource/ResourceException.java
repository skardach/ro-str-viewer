package com.skardach.ro.resource;

/**
 * Usually thrown when there is a resource missing (i.e. texture file could not
 * be opened).
 * @author Stanislaw Kardach
 *
 */
public class ResourceException extends Exception {
	private static final long serialVersionUID = 2678539482454816082L;
	/**
	 * Default constructor with a custom error message.
	 * @param string Error message to be passed to the Exception constructor.
	 */
	public ResourceException(String string) {
		super(string);
	}
}