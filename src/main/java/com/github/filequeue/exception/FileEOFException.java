package com.github.filequeue.exception;

public class FileEOFException extends Exception {

	private static final long serialVersionUID = 4701796168682302255L;

	public FileEOFException() {
		super();
	}
	public FileEOFException(String message) {
		super(message);
	}

	public FileEOFException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileEOFException(Throwable cause) {
		super(cause);
	}
	@Override
	public  Throwable fillInStackTrace() {
		return this;
	}

}
