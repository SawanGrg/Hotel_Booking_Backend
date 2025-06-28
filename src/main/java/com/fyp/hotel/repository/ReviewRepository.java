package com.fyp.hotel.repository;

import com.fyp.hotel.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN FETCH r.hotel h WHERE h.hotelId = :hotelId")
    List<Review> findByHotelId(@Param("hotelId") Long hotelId);
}
