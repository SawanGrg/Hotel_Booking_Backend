package com.fyp.hotel.service.user.booking;

import com.fyp.hotel.dto.booking.BookDto;
import com.fyp.hotel.dto.booking.BookingDTO;
import com.fyp.hotel.dto.booking.BookingStatusDTO;
import com.fyp.hotel.dto.khalti.KhaltiResponseDTO;
import com.fyp.hotel.dto.room.CheckRoomAvailabilityDto;

import java.util.List;

public interface UserBookingService {
    String cashOnArrivalTransaction(BookDto bookDto);
    KhaltiResponseDTO onlinePaymentTransaction(BookDto bookDto);
    String updatePaymentTransaction(String pidx, String status, String bookingId, long totalAmount);
    CheckRoomAvailabilityDto checkRoomAvailability(Long roomId);
    List<BookingDTO> viewBookingDetails();
    List<BookingStatusDTO> getBookingStatus(Long hotelId);
}