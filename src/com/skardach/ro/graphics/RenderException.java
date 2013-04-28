/**
 *
 */
package com.skardach.ro.graphics;

/**
 * Thrown whenever there is something wrong with rendering process.
 * @author Stanislaw Kardach
 *
 */
public class RenderException extends Exception {
	private static final long serialVersionUID = -1711254889216629098L;
	/**
	 * Create exception with given error message.
	 * @param string error message
	 */
	public RenderException(String string) {
		super(string);
	}
}
