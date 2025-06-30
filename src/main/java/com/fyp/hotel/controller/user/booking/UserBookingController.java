package com.fyp.hotel.controller.user.booking;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.booking.BookingDTO;
import com.fyp.hotel.dto.booking.BookingStatusDTO;
import com.fyp.hotel.dto.booking.BookDto;
import com.fyp.hotel.dto.khalti.KhaltiResponseDTO;
import com.fyp.hotel.service.user.UserService;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserBookingController {

    private UserService userService;
    private ValueMapper valueMapper;

    @Autowired
    public UserBookingController(UserService userService, ValueMapper valueMapper) {
        this.userService = userService;
        this.valueMapper = valueMapper;
    }

    @GetMapping("/bookingStatus/{hotelId}")
    public ResponseEntity<?> getBookingStatus(@PathVariable Long hotelId) {
        List<BookingStatusDTO> statuses = userService.getBookingStatus(hotelId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", statuses));
    }

    @GetMapping("/viewBookingDetails")
    public ResponseEntity<?> viewBookingDetails() {
        List<BookingDTO> bookings = userService.viewBookingDetails();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", bookings));
    }

    @PostMapping("/payment/{roomId}")
    public ResponseEntity<?> processPayment(
            @PathVariable Long roomId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam(required = false) String daysOfStay,
            @RequestParam(defaultValue = "2") String numberOfGuest,
            @RequestParam String paymentMethod)
    {
        BookDto bookDto = valueMapper.mapToBooking(roomId, checkInDate, checkOutDate, numberOfGuest, paymentMethod);
        String response = userService.cashOnArrivalTransaction(bookDto);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }

    @PostMapping("/khalti/payment/{roomId}")
    public KhaltiResponseDTO processKhaltiPayment(
            @PathVariable Long roomId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam(defaultValue = "2") String numberOfGuest,
            @RequestParam String paymentMethod)
    {
        BookDto bookDto = valueMapper.mapToBooking(roomId, checkInDate, checkOutDate, numberOfGuest, paymentMethod);
        return userService.onlinePaymentTransaction(bookDto);
    }

    @PostMapping("/khalti/update")
    public ResponseEntity<?> updateKhaltiPayment(
            @RequestParam String pidx,
            @RequestParam String status,
            @RequestParam String bookingId,
            @RequestParam long totalAmount) {
        String response = userService.updatePaymentTransaction(pidx, status, bookingId, totalAmount);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }
}