package com.fyp.hotel.controller.vendor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.service.vendor.vendorImpl.VendorServiceImplementation;
import com.fyp.hotel.util.ValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fyp.hotel.dto.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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
    ) {
        String response = this.vendorServiceImplementation.updateBooking(bookingId, userId, status);
        if ("Successfully Changed".equals(response)) {
            ApiResponse<String> apiResponse = new ApiResponse<>(200, "Successfully Booked", response);
            return ResponseEntity.ok().body(apiResponse);
        } else {
            ApiResponse<String> apiResponse = new ApiResponse<>(400, "Failed to update status", response);
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }
}