package com.fyp.hotel.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fyp.hotel.dto.error.ErrorDto;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HotelExceptionHandler {
    
    @ExceptionHandler( value = HotelAlreadyExist.class )
    public ResponseEntity<ErrorDto> hotelAlreadyExist(HotelAlreadyExist ex){

        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(ex.getMessage());
        errorDto.setStatus( 
            //httpstatus is enum
            //BAD_REQUEST is enum
            //.valueof() is used to get the value of enum
            HttpStatus.valueOf(HttpStatus.BAD_REQUEST.value()) // this line converts BAD_REQUEST to 400
        );
        errorDto.setError("BAD_REQUEST");
        errorDto.setPath("/vendor/home");
        errorDto.setTimeStamp(ZonedDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception ex) {
//        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
