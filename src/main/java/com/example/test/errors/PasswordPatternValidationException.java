package com.example.test.errors;

public class PasswordPatternValidationException extends Exception {
    public PasswordPatternValidationException() {
    }

    public PasswordPatternValidationException(String message) {
        super(message);
    }

    public PasswordPatternValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordPatternValidationException(Throwable cause) {
        super(cause);
    }

    public PasswordPatternValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
