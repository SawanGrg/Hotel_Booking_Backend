package com.fyp.hotel.controller.vendor;

import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.util.ValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.service.vendor.vendorImpl.VendorServiceImplementation;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) // maxage used to cache where the browser will not send the preflight request
@RequestMapping("/v1/vendor")
public class DeleteRoomController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VendorServiceImplementation vendorServiceImplementation;

    @Autowired
    private ValueMapper valueMapper;


    private static final Logger logger = LoggerFactory.getLogger(VendorController.class);


    //delete a specific room
    @PostMapping("/delete/{hotelId}")
    public ResponseEntity<?> deleteRoom(
            @PathVariable(name = "hotelId") Long hotelId
    ){
        try {
            logger.info("Attempting to delete room with ID: {}", hotelId);
            String response = vendorServiceImplementation.deleteSpecificRoom(hotelId);

            if ("Room deleted successfully".equals(response)) {
                logger.info("Room deleted successfully with ID: {}", hotelId);
                ApiResponse<String> apiResponse = new ApiResponse<>(200, "Success", response);
                return ResponseEntity.ok(apiResponse);
            } else {
                logger.error("Failed to delete room with ID {}: {}", hotelId, response);
                ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", response);
                return ResponseEntity.status(500).body(apiResponse);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while deleting room with ID {}: {}", hotelId, e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

}
