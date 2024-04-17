package com.fyp.hotel.exception;

import java.util.Map;

public class DTOValidationException extends RuntimeException {

    private Map<String, String> errors;

    public DTOValidationException(Map<String, String> errors) {
        super("DTO validation error");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
