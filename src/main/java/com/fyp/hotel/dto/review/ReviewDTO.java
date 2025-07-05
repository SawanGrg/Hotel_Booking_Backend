package com.fyp.hotel.dto.review;

import com.fyp.hotel.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {

    private long reviewId;
    private String reviewContent;
    private long hotelId;
    private User user;
    private String createdDate;

    public ReviewDTO() {
    }

    public ReviewDTO(long reviewId, String reviewContent, long hotelId, User user, String createdDate) {
        this.reviewId = reviewId;
        this.reviewContent = reviewContent;
        this.hotelId = hotelId;
        this.user = user;
        this.createdDate = createdDate;
    }

}
