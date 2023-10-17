package com.fyp.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.User;


@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Hotel findByUser(User user);
    
}
