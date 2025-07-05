package com.fyp.hotel.service.vendor.booking.Impl;

import com.fyp.hotel.dao.booking.BookingDAO;
import com.fyp.hotel.dao.hotel.HotelDAO;
import com.fyp.hotel.dto.booking.BookingStatusDTO;
import com.fyp.hotel.dto.room.RoomHistoryDTO;
import com.fyp.hotel.dto.vendorDto.VendorBookingDTO;
import com.fyp.hotel.model.*;
import com.fyp.hotel.repository.*;
import com.fyp.hotel.service.vendor.booking.VendorBookingService;
import com.fyp.hotel.util.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorBookingServiceImpl implements VendorBookingService {

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final HotelRoomRepository hotelRoomRepository;
    private final HotelDAO hotelDAO;
    private final BookingRepository bookingRepository;
    private final BookingDAO bookingDAO;
    private final EmailSenderService emailSenderService;

    @Override
    @Transactional(readOnly = true)
    public List<VendorBookingDTO> getBookings(String status, int page, int size) {
        Hotel hotel = getCurrentVendorHotel();
        List<Booking> bookings = hotelDAO.getBookingDetails(status, hotel.getHotelId(), page, size);

        List<VendorBookingDTO> bookingDTOs = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDTOs.add(convertToVendorBookingDTO(booking));
        }
        return bookingDTOs;
    }

    @Override
    @Transactional
    public String updateBooking(long bookingId, long userId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean updated = hotelDAO.updateBookingStatus(bookingId, status);
        if (!updated) {
            return null;
        }

        if ("BOOKED".equals(status)) {
            HotelRoom room = booking.getHotelRoom();
            room.setRoomStatus("BOOKED");
            hotelRoomRepository.save(room);
        }

        sendStatusUpdateEmail(user, status);
        return "Successfully Changed";
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingStatusDTO> getBookingStatusDetails(long roomId) {
        return bookingDAO.getBookingStatusDetailsByRoomId(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomHistoryDTO> getRoomHistory(long roomId) {
        List<Booking> bookings = bookingRepository.findByHotelRoom_RoomId(roomId);
        List<RoomHistoryDTO> historyDTOs = new ArrayList<>();
        for (Booking booking : bookings) {
            historyDTOs.add(convertToRoomHistoryDTO(booking));
        }
        return historyDTOs;
    }

    private Hotel getCurrentVendorHotel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User vendor = userRepository.findByUserName(username);
        return hotelRepository.findByUser(vendor);
    }

    private VendorBookingDTO convertToVendorBookingDTO(Booking booking) {
        HotelRoom room = booking.getHotelRoom();

        return VendorBookingDTO.builder()
                .bookingId(booking.getBookingId())
                .checkInDate(booking.getCheckInDate().toString())
                .checkOutDate(booking.getCheckOutDate().toString())
                .bookingDate(booking.getBookingDate().toString())
                .status(booking.getStatus().toString())
                .paymentMethod(booking.getPaymentMethod().getPaymentMethodName())
                .totalAmount(booking.getTotalAmount())
                .roomId(room.getRoomId())
                .bedCategory(room.getRoomBed().toString())
                .roomPrice(room.getRoomPrice().toString())
                .roomType(room.getRoomType().toString())
                .roomDescription(room.getRoomDescription())
                .hotelRoomImage(room.getMainRoomImage())
                .bedType(room.getRoomBed().toString())
                .hasAC(room.getHasAC())
                .hasBalcony(room.getHasBalcony())
                .hasRefridge(room.getHasRefridge())
                .hasTV(room.getHasTV())
                .user(booking.getUser())
                .vendorUpdated(booking.getVendorUpdated())
                .build();
    }

    private RoomHistoryDTO convertToRoomHistoryDTO(Booking booking) {
        return RoomHistoryDTO.builder()
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .paymentMethod(booking.getPaymentMethod().getPaymentMethodName())
                .totalAmount(booking.getTotalAmount())
                .userName(booking.getUser().getUsername())
                .build();
    }

    private void sendStatusUpdateEmail(User user, String status) {
        String emailSubject = "Booking status updated";
        String emailBody = "Your booking status has been updated to: " + status;
        emailSenderService.sendEmail(user.getUserEmail(), emailSubject, emailBody);
    }
}