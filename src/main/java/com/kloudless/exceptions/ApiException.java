package com.kloudless.exceptions;

public class ApiException extends KloudlessException {
    private static final long serialVersionUID = 1L;

    public ApiException(String message, Exception e) {
        super(message, e);
    }
}
