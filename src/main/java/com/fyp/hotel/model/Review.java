package com.fyp.hotel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //identity means auto increment by 1 it will be in sequence and it will be unique
    private long reviewId;

    @Column(name = "review_content", nullable = false, length = 50)
    private String reviewContent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "hotel_id",
            nullable = false,
            unique = false
    )
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = false
    )
    private User user;

    @Column(name = "created_date", nullable = false)
    private String createdDate;

    public Review() {
    }

    public Review(String reviewContent, Hotel hotel, User user) {
        this.reviewContent = reviewContent;
        this.hotel = hotel;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", reviewContent='" + reviewContent + '\'' +
                ", hotel=" + hotel +
                ", user=" + user +
                '}';
    }
}
