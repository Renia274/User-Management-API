package com.example.test;

import com.example.test.errors.PasswordPatternValidationException;
import com.example.test.errors.UsernamePatternValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PasswordPatternValidationException.class)
    public ResponseEntity<Object> handlePassException(PasswordPatternValidationException ex) {
        // Create an appropriate response entity or error message
        String errorMessage = "Validation error: " + ex.getMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(UsernamePatternValidationException.class)
    public ResponseEntity<Object> handleUsernameException(UsernamePatternValidationException ex) {
        // Create an appropriate response entity or error message
        String errorMessage = "Validation error: " + ex.getMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
