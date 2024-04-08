package com.fyp.hotel.controller.user;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.BookingDTO;
import com.fyp.hotel.dto.CheckRoomAvailabilityDto;
import com.fyp.hotel.dto.DisplayHotelWithAmenitiesDto;
import com.fyp.hotel.dto.khalti.KhaltiResponseDTO;
import com.fyp.hotel.dto.userDto.*;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.dto.vendorDto.RoomDto;
import com.fyp.hotel.model.*;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;
import com.fyp.hotel.util.ValueMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
