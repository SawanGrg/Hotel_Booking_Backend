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
    
    //zoneddatetime is used to store date and time with timezone
    //instant is used to store date and time without timezone
    private ZonedDateTime timeStamp; 
    private HttpStatus status;
    // private List<String> error;
    private String error;
    private String message;
    private String path;

    // private String message;
    // private HttpStatus status;
    // private Instant timeStamp;

}
