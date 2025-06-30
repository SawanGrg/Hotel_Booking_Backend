package com.fyp.hotel.controller.user.review;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.hotel.HotelReviewDTO;
import com.fyp.hotel.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserReviewController {

    @Autowired
    private UserService userServiceImpl;

    @GetMapping("/hotelReview/{hotelId}")
    public ResponseEntity<?> viewHotelReview(@PathVariable long hotelId) {
        List<HotelReviewDTO> reviews = userServiceImpl.getAllHotelReviews(hotelId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", reviews));
    }

    @PostMapping("/review/{hotelId}")
    public ResponseEntity<?> postHotelReview(
            @PathVariable long hotelId,
            @RequestBody HotelReviewDTO reviewDTO) {
        String response = userServiceImpl.postHotelReviewByUser(hotelId, reviewDTO);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }
}