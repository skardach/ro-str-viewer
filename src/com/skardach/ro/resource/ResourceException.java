package com.skardach.ro.resource;

/**
 * Usually thrown when there is a resource missing (i.e. texture file could not
 * be opened).
 * @author Stanislaw Kardach
 *
 */
public class ResourceException extends Exception {
	private static final long serialVersionUID = 2678539482454816082L;

	public ResourceException(String string) {
		super(string);
	}
}