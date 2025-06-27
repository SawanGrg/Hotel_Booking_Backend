package com.fyp.hotel.controller.user;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.userDto.*;
import com.fyp.hotel.service.user.userImpl.UserServiceImplementation;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/v1/user")
public class HotelReviewController {

    @Autowired
    private UserServiceImplementation userServiceImplementation;
    @Autowired
    private  UserProfileDto userProfileDto;
    @Autowired
    private ValueMapper valueMapper;
    @Autowired
    private ObjectMapper objectMapper;

    // user post reviews the specific hotel
    @PostMapping("/review/{hotelId}")
    public ResponseEntity<?> postHotelReview(
            @PathVariable(name = "hotelId") long hotelId,
            @RequestBody HotelReviewDTO hotelReviewDTO
    ) {
        try {
            String response = userServiceImplementation.postHotelReviewByUser(hotelId, hotelReviewDTO);

            if ("Review posted successfully.".equals(response)) { // Check the response message here
                ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
                return ResponseEntity.status(200).body(successResponse);
            } else {
                ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", response);
                return ResponseEntity.status(500).body(errorResponse);
            }
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
