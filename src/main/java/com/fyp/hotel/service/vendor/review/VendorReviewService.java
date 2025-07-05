package com.fyp.hotel.service.vendor.review;

import com.fyp.hotel.dto.review.ReviewDTO;

import java.util.List;

public interface VendorReviewService {
    List<ReviewDTO> getHotelReviews();
}