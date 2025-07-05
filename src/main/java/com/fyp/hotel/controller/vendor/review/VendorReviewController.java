package com.fyp.hotel.controller.vendor.review;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.review.ReviewDTO;
import com.fyp.hotel.service.vendor.VendorServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/vendor")
public class VendorReviewController {

    @Autowired
    private VendorServiceFacade vendorService;

    @GetMapping("/hotelReview")
    public ResponseEntity<?> getHotelReview() {
        List<ReviewDTO> reviews = vendorService.getVendorReviewService().getHotelReviews();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", reviews));
    }
}