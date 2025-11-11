package com.example.demo.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ResponseInterface {

    // Generic method returning a ResponseEntity with any body type
    <T> ResponseEntity<T> createResponse(T body, HttpStatus status);

    // Overloaded method specialized for objects with a message field
    ResponseEntity<MessageResponse> createResponse(MessageResponse body, HttpStatus status);
}
