
package com.github.filequeue.exception;

public class FileFormatException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6950322066714479555L;

	/**
	 * Constructs an {@code FileFormatException} with {@code null} as its error
	 * detail message.
	 */
	public FileFormatException() {
		super();
	}

	public FileFormatException(String message) {
		super(message);
	}

	public FileFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileFormatException(Throwable cause) {
		super(cause);
	}
}
