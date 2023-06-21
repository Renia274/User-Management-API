package com.example.test;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
        // Get the status code and error message from the request attributes
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String message = (String) request.getAttribute("javax.servlet.error.message");

        // Create an ErrorResponse object with the status code and message
        ErrorResponse errorResponse = new ErrorResponse(statusCode, message);

        // Return a ResponseEntity containing the ErrorResponse and corresponding HttpStatus
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(statusCode));
    }

    // Get the path for handling errors
    public String getErrorPath() {
        return "/error";
    }

    // ErrorResponse class to hold the status code and message
    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
