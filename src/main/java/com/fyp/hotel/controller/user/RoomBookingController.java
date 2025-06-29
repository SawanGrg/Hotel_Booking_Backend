package com.fyp.hotel.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.booking.BookDto;
import com.fyp.hotel.dto.user.UserProfileDto;
import com.fyp.hotel.service.user.userImpl.UserServiceImpl;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class RoomBookingController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserProfileDto userProfileDto;

    @Autowired
    private ValueMapper valueMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/payment/{roomId}")
    public ResponseEntity<?> paymentGateway(
            @PathVariable(name = "roomId") Long roomId,
            @RequestParam(name = "checkInDate") String checkInDate,
            @RequestParam(name = "checkOutDate") String checkOutDate,
            @Validated @RequestParam(name = "daysOfStay", required = false) String daysOfStay,
            @RequestParam(name = "numberOfGuest", required = false, defaultValue = "2") String numberOfGuest,
            @RequestParam(name = "paymentMethod") String paymentMethod
    ) {
        BookDto bookDto = valueMapper.mapToBooking(roomId, checkInDate, checkOutDate, numberOfGuest, paymentMethod);
        String response = userServiceImpl.hotelPaymentGateWay(bookDto);

        if ("Payment successful by cash on arrival".equals(response) ||
                "Payment successful by khalti".equals(response)) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
        } else {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "An error occurred", response));
        }
    }
}
