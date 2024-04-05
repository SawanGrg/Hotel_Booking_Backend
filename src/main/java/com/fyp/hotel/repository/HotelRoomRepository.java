package com.fyp.hotel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fyp.hotel.model.HotelRoom;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface HotelRoomRepository extends JpaRepository<HotelRoom, Long>{

    //get hotel rooms details by hotel id
    List<HotelRoom> findHotelRoomByHotel_HotelId(Long hotelId);

    // Get hotel rooms details by hotel id where is_deleted is false
    @Query("SELECT hr FROM HotelRoom hr WHERE hr.hotel.hotelId = :hotelId AND hr.isDeleted = false")
    List<HotelRoom> findActiveHotelRoomsByHotelId(@Param("hotelId") Long hotelId);

    //get hotel rooms details by hotel id using custom query
//    @Query("SELECT hr FROM HotelRoom hr WHERE hr.hotel.hotelId = :hotelId")
//    List<HotelRoom> findHotelRoomsByHotelId(@Param("hotelId") Long hotelId);

    // Get hotel rooms details by hotel id with pagination
    Page<HotelRoom> findByHotel_HotelId(Long hotelId, Pageable pageable);

    HotelRoom findByHotel_HotelId(Long hotelId);
    @Transactional
    @Modifying // modify the database
    @Query("DELETE FROM HotelRoom hr WHERE hr.roomId = :wantedRoomId")
    void deleteRoomById(@Param("wantedRoomId") Long roomId);

    @Query("SELECT hr FROM HotelRoom hr WHERE hr.hotel.hotelId = :hotelId AND hr.isDeleted = false")
    Page<HotelRoom> findAllByHotel_HotelId(Long hotelId, Pageable pageable);

    //get hotel room details by room id
    HotelRoom findByRoomId(Long roomId);

    //get hotel room details by booking id
    HotelRoom findByBooking_BookingId(Long bookingId);

}
