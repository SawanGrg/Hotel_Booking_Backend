package com.fyp.hotel.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.userDto.HotelReviewDTO;
import com.fyp.hotel.dto.userDto.UserProfileDto;
import com.fyp.hotel.service.user.userImpl.UserServiceImpl;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class HotelReviewController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserProfileDto userProfileDto;

    @Autowired
    private ValueMapper valueMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/review/{hotelId}")
    public ResponseEntity<?> postHotelReview(
            @PathVariable(name = "hotelId") long hotelId,
            @RequestBody HotelReviewDTO hotelReviewDTO
    ) {
        String response = userServiceImpl.postHotelReviewByUser(hotelId, hotelReviewDTO);

        if ("Review posted successfully.".equals(response)) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
        } else {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "An error occurred", response));
        }
    }
}
