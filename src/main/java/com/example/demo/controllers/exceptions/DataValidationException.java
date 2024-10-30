package com.example.demo.controllers.exceptions;

public class DataValidationException extends RuntimeException {
    
    public DataValidationException() {
        super();
    }

    public DataValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DataValidationException(final String message) {
        super(message);
    }

    public DataValidationException(final Throwable cause) {
        super(cause);
    }

}
