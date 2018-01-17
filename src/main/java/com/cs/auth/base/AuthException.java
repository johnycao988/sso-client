package com.cs.auth.base;

public class AuthException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthException(String message) {
		super(message);
	}

	public AuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthException(Exception e) {
		super(e);
	}

}
