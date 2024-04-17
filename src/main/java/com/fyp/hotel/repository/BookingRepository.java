package com.fyp.hotel.repository;

import com.fyp.hotel.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
        @Query("SELECT b FROM Booking b JOIN FETCH b.hotelRoom WHERE b.user.userId = :userId")
        List<Booking> findByUserWithHotelRoom(@Param("userId") Long userId);

        Booking findByBookingId(long bookingId);

    List<Booking> findByHotelRoom_RoomId(long roomId);

}

