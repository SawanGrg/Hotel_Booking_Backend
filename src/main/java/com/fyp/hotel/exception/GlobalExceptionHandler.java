package com.fyp.hotel.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DTOValidationException.class)
    public ResponseEntity<Map<String, Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {

        List<FieldError> errors = ex.getBindingResult().getFieldErrors();

        Map<String, String> errorFields = new HashMap<>();

        for (FieldError error : errors) {
            errorFields.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Map<String, String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errorFields);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}

