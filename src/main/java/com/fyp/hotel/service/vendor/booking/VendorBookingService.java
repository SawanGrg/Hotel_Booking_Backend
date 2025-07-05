package com.fyp.hotel.service.vendor.booking;

import com.fyp.hotel.dto.booking.BookingStatusDTO;
import com.fyp.hotel.dto.room.RoomHistoryDTO;
import com.fyp.hotel.dto.vendorDto.VendorBookingDTO;

import java.util.List;

public interface VendorBookingService {
    List<VendorBookingDTO> getBookings(String status, int page, int size);
    String updateBooking(long bookingId, long userId, String status);
    List<BookingStatusDTO> getBookingStatusDetails(long roomId);
    List<RoomHistoryDTO> getRoomHistory(long roomId);
}