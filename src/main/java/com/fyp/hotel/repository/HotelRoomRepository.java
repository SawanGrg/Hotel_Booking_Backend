package com.fyp.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fyp.hotel.model.HotelRoom;

@Repository
public interface HotelRoomRepository extends JpaRepository<HotelRoom, Long>{
    
}
