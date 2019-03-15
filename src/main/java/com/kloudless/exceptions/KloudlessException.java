package com.kloudless.exceptions;

import java.util.logging.Logger;

abstract class KloudlessException extends Exception {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(KloudlessException.class.getName());

    public KloudlessException(String message, Exception e) {
        super(message);
        if (e != null) {
            logger.info(message + " : " + e.getMessage());
        } else {
            logger.info(message);
        }
    }
}
