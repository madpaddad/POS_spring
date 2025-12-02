package com.example.demo.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Central Exception Handler for the entire application
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles the HttpMediaTypeNotAcceptableException (406 Not Acceptable).
     * This occurs when the client's 'Accept' header cannot be fulfilled by the server's converters.
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotAcceptableException() {
        String message = "The server cannot generate a response in a format acceptable to the client (based on the 'Accept' header). Please ensure you set 'Accept: application/json'.";

        // You might want to define a specific DTO for errors
        ErrorResponse error = new ErrorResponse(406, message);

        // Return the error message with a 406 Not Acceptable status code
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }
}

// Example DTO for a consistent error structure
class ErrorResponse {
    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
    // Getters and setters (or records in modern Java)
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    // ...
}
