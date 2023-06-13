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
        // Retrieve the status code and error message from the request attributes
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String message = (String) request.getAttribute("javax.servlet.error.message");
        
        // Create an instance of ErrorResponse using the status code and error message
        ErrorResponse errorResponse = new ErrorResponse(statusCode, message);
        
        // Return a ResponseEntity containing the error response and appropriate HTTP status code
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(statusCode));
    }

    // Implement the getErrorPath() method required by the ErrorController interface
    public String getErrorPath() {
        return "/error";
    }

    // Define a nested class for representing the error response
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
