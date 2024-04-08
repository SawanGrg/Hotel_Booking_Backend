package com.fyp.hotel.controller.vendor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.userDto.UserProfileDto;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;
import com.fyp.hotel.serviceImpl.vendor.VendorServiceImplementation;
import com.fyp.hotel.util.ValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) // maxage used to cache where the browser will not send the preflight request
@RequestMapping("/v1/vendor")
public class VerifyBookingController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VendorServiceImplementation vendorServiceImplementation;
    @Autowired
    private ValueMapper valueMapper;

    private static final Logger logger = LoggerFactory.getLogger(VendorController.class);

    @PostMapping("/roomStatus/{bookingId}/{userId}")
    public ResponseEntity<?> changeStatus(
            @PathVariable(name = "bookingId", required = true) long bookingId,
            @PathVariable(name = "userId", required = true) long userId,
            @RequestParam(name = "vendorDecision", required = true, defaultValue = "BOOKED") String status
    ){
        try {
            // Call service to update booking status
            String response = this.vendorServiceImplementation.updateBooking(bookingId,userId, status);

            // Check if the booking status was successfully changed
            if ("Successfully Changed".equals(response)) {
                ApiResponse<String> apiResponse = new ApiResponse<>(200, "Successfully Booked", response);
                return ResponseEntity.ok().body(apiResponse);
            } else {
                // Handle if status update fails
                ApiResponse<String> apiResponse = new ApiResponse<>(400, "Failed to update status", response);
                return ResponseEntity.badRequest().body(apiResponse);
            }
        } catch (Exception e) {
            // Handle any exceptions
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Internal Server Error", e.getMessage());
            return ResponseEntity.status(500).body(apiResponse);
        }
    }
}
