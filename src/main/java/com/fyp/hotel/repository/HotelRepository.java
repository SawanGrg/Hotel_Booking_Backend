package com.fyp.hotel.repository;

import com.fyp.hotel.model.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.User;

import java.util.List;


@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Hotel findByUser(User user);
    Hotel findHotelByHotelId(Long hotelId);

}
