package com.github.filequeue.exception;
public class ConfigException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5680376999772385539L;
	public ConfigException() { 
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
		
	}

}
