package com.fyp.hotel.repository;

import com.fyp.hotel.model.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fyp.hotel.model.RoomImage;

import java.util.List;

@Repository
public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {

    List<RoomImage> findImageUrlByHotelRoom_RoomId(Long roomId);

//    @Modifying
//   @Query()
//    void deleteImageUrlByHotelRoom_RoomId(long roomId);

    @Modifying
    @Query("DELETE FROM RoomImage r WHERE r.hotelRoom.roomId = :roomId")
    void deleteImageUrlByHotelRoom_RoomId(long roomId);

    List<RoomImage> findByHotelRoom(HotelRoom room);


    List<RoomImage> findByHotelRoom_RoomId(Long hotelId);
}
