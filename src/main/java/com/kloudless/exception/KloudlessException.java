package com.kloudless.exception;

public abstract class KloudlessException extends Exception {

	private static final long serialVersionUID = 1L;

	public KloudlessException(String message) {
		super(message, null);
	}

	public KloudlessException(String message, Throwable e) {
		super(message, e);
	}
}
