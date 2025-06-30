package com.fyp.hotel.exception;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fyp.hotel.dto.error.ErrorDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e
    ) throws IOException {


        ErrorDto errorDto = new ErrorDto();


        errorDto.setTimeStamp(ZonedDateTime.now());

        errorDto.setStatus(
                HttpStatus.valueOf(HttpServletResponse.SC_UNAUTHORIZED)
        );

        errorDto.setError(
                // List.of("UNAUTHORIZED")
                "UNAUTHORIZED"
        );

        errorDto.setMessage(
                e.getMessage()
        );

        errorDto.setPath(
                httpServletRequest.getServletPath()
        );

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        httpServletResponse.getOutputStream().println(
                objectMapper.writeValueAsString(errorDto) // this line converts errorDto to json
        );

    }
}