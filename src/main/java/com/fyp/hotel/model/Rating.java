package com.fyp.hotel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //identity means auto increment by 1 it will be in sequence and it will be unique
    @Column(name = "rating_id")
    private long ratingId;

    @Column(name = "rating_value", nullable = false, length = 50)
    private long ratingValue;

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

    public Rating() {
    }

    public Rating(long ratingValue, Hotel hotel, User user) {
        this.ratingValue = ratingValue;
        this.hotel = hotel;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ratingId=" + ratingId +
                ", ratingValue='" + ratingValue + '\'' +
                ", hotel=" + hotel +
                ", user=" + user +
                '}';
    }
}
