package com.kloudless.exception;

public abstract class KloudlessException extends Exception {

	public KloudlessException(String message) {
		super(message, null);
	}

	public KloudlessException(String message, Throwable e) {
		super(message, e);
	}
	
	private static final long serialVersionUID = 1L;
}
