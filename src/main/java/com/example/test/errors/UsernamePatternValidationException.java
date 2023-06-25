package com.example.test.errors;

public class UsernamePatternValidationException extends Exception {
    public UsernamePatternValidationException() {
    }

    public UsernamePatternValidationException(String message) {
        super(message);
    }

    public UsernamePatternValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernamePatternValidationException(Throwable cause) {
        super(cause);
    }

    public UsernamePatternValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
