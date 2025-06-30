package com.fyp.hotel.controller.vendor.booking;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.vendorDto.VendorBookingDTO;
import com.fyp.hotel.service.vendor.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/vendor")
public class VendorBookingController {

    @Autowired
    private VendorService vendorService;

    @GetMapping("/bookings")
    public ResponseEntity<?> getBookings(
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "30") int size
    ) {
        List<VendorBookingDTO> vendorBookingDTOS = vendorService.getBookings(status, page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", vendorBookingDTOS));
    }

    @PostMapping("/roomStatus/{bookingId}/{userId}")
    public ResponseEntity<?> changeStatus(
            @PathVariable(name = "bookingId") long bookingId,
            @PathVariable(name = "userId") long userId,
            @RequestParam(name = "vendorDecision", defaultValue = "BOOKED") String status
    ) {
        String response = vendorService.updateBooking(bookingId, userId, status);
        if ("Successfully Changed".equals(response)) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Successfully Booked", response));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Failed to update status", response));
    }

    @GetMapping("/bookingStatus/{roomId}")
    public ResponseEntity<?> getBookingStatus(@PathVariable long roomId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", vendorService.getBookingStatusDetails(roomId)));
    }
}