package com.fyp.hotel.controller.vendor;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.service.vendor.vendorImpl.VendorServiceImplementation;

@RestController
@RequestMapping("/v1/vendor")
public class DeleteRoomController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VendorServiceImplementation vendorServiceImplementation;
    @Autowired
    private ValueMapper valueMapper;

    @PostMapping("/delete/{hotelId}")
    public ResponseEntity<?> deleteRoom(
            @PathVariable(name = "hotelId") Long hotelId
    ) {
        String response = vendorServiceImplementation.deleteSpecificRoom(hotelId);
        if ("Room deleted successfully".equals(response)) {
            ApiResponse<String> apiResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.ok(apiResponse);
        } else {
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", response);
            return ResponseEntity.status(500).body(apiResponse);
        }
    }
}