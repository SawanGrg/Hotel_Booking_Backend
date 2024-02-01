package com.fyp.hotel.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ValidationHandler extends ResponseEntityExceptionHandler{

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, //this exception is thrown when argument annotated with @valid failed validation
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request //webrequest is used to get the request that is coming from the client
    ) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                //getbindingresult is helpful to get the error message thrown by the validator such as @notnull, @notempty, @size, @pattern, etc
                .getAllErrors()
                //getallerrors is used to get all the errors thrown by the validator
                .forEach((error) ->{

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }
}