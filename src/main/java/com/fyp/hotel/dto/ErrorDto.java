package com.fyp.hotel.dto;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    

    private ZonedDateTime timeStamp; 
    private HttpStatus status;
    private String error;
    private String message;
    private String path;

}
