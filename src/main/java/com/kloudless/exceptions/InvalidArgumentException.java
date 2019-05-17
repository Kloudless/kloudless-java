package com.kloudless.exceptions;

public class InvalidArgumentException extends KloudlessException {
    private static final long serialVersionUID = 1L;

    public InvalidArgumentException(String message, Exception e) {
        super(message, e);
    }
}
