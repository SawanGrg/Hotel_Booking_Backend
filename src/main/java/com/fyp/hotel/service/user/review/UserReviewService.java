package com.fyp.hotel.service.user.review;

import com.fyp.hotel.dto.hotel.HotelReviewDTO;

import java.util.List;

public interface UserReviewService {
    String postHotelReviewByUser(long hotelId, HotelReviewDTO hotelReviewDTO);
    List<HotelReviewDTO> getAllHotelReviews(Long hotelId);
}