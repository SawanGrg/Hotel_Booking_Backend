package com.fyp.hotel.dto.userDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelReviewDTO {

    private long hotelId;
    private String hotelReview;
    private String userName;
    private String createdDate;

    public HotelReviewDTO() {
    }

    public HotelReviewDTO(long hotelId, String hotelReview, String userName, String createdDate) {
        this.hotelId = hotelId;
        this.hotelReview = hotelReview;
        this.userName = userName;
        this.createdDate = createdDate;
    }

}
