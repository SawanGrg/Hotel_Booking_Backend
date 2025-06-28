package com.fyp.hotel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fyp.hotel.model.HotelRoom;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface HotelRoomRepository extends JpaRepository<HotelRoom, Long>{
    @Query("SELECT hr FROM HotelRoom hr WHERE hr.hotel.hotelId = :hotelId AND hr.isDeleted = false")
    List<HotelRoom> findActiveHotelRoomsByHotelId(@Param("hotelId") Long hotelId);
    @Query("SELECT hr FROM HotelRoom hr WHERE hr.hotel.hotelId = :hotelId AND hr.isDeleted = false")
    Page<HotelRoom> findAllByHotel_HotelId(Long hotelId, Pageable pageable);
    HotelRoom findByRoomId(Long roomId);
    HotelRoom findByBooking_BookingId(Long bookingId);

}
