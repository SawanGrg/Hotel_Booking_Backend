package com.fyp.hotel.controller.user.review;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.hotel.HotelReviewDTO;
import com.fyp.hotel.service.user.UserServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserReviewController {

    private UserServiceFacade userServiceFacade;

    @Autowired
    public UserReviewController(UserServiceFacade userServiceFacade) {
        this.userServiceFacade = userServiceFacade;
    }

    @GetMapping("/hotelReview/{hotelId}")
    public ResponseEntity<?> viewHotelReview(@PathVariable long hotelId) {
        List<HotelReviewDTO> reviews = this.userServiceFacade.userReviewService.getAllHotelReviews(hotelId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", reviews));
    }

    @PostMapping("/review/{hotelId}")
    public ResponseEntity<?> postHotelReview(
            @PathVariable long hotelId,
            @RequestBody HotelReviewDTO reviewDTO) {
        String response = this.userServiceFacade.userReviewService.postHotelReviewByUser(hotelId, reviewDTO);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }
}