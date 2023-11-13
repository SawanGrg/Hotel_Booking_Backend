package com.fyp.hotel.dto;

public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T body;

    public  ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.body = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public T getBody() {
        return body;
    }
}

