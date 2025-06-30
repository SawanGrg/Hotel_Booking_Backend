package com.fyp.hotel.controller.vendor.hotel;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.hotel.HotelDto;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.service.vendor.VendorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/vendor")
public class VendorHotelController {

    @Autowired
    private VendorService vendorService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/hotelDetails")
    public ResponseEntity<?> getHotelDetails() {
        Hotel hotel = vendorService.getHotelDetailsService();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", hotel));
    }

    @PostMapping("/updateHotelDetails")
    public ResponseEntity<?> updateHotelDetails(
            @RequestParam("hotelData") String hotelDataJson,
            @RequestParam(value = "mainHotelImage", required = false) MultipartFile mainHotelImage
    ) throws Exception {
        HotelDto hotelDto = objectMapper.readValue(hotelDataJson, HotelDto.class);
        String response = vendorService.updateHotelDetails(
                hotelDto.getHotelName(),
                hotelDto.getHotelAddress(),
                hotelDto.getHotelContact(),
                hotelDto.getHotelEmail(),
                hotelDto.getHotelPan(),
                hotelDto.getHotelDescription(),
                hotelDto.getHotelStar(),
                mainHotelImage
        );
        if ("Hotel details updated successfully".equals(response)) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
        }
        return ResponseEntity.status(500).body(new ApiResponse<>(500, "Failed", response));
    }
}