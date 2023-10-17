package com.fyp.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fyp.hotel.model.RoomImage;

@Repository
public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {

}
