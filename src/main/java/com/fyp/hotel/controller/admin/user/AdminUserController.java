// AdminUserController.java
package com.fyp.hotel.controller.admin.user;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.booking.BookingDTO;
import com.fyp.hotel.dto.common.PaginationRequestDTO;
import com.fyp.hotel.model.User;
import com.fyp.hotel.service.admin.AdminServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class AdminUserController {

    @Autowired
    private AdminServiceFacade adminServiceFacade;

    @GetMapping("/viewAllUsers")
    public ResponseEntity<?> viewAllUsers(
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(defaultValue = "", required = false) String username,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<User> users = adminServiceFacade.userService.getAllUserFilter(username, ascending, page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", users));
    }

    @GetMapping("/viewAllVendors")
    public ResponseEntity<?> viewAllVendors(
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(defaultValue = "", required = false) String username,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<User> vendors = adminServiceFacade.userService.getAllVendorFilter(username, ascending, page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", vendors));
    }

    @GetMapping("/getUserProfile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable long userId) {
        User user = adminServiceFacade.userService.getUserProfile(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", user));
    }

    @PostMapping("/unverifyUser/{userId}")
    public ResponseEntity<?> unVerifyUser(
            @PathVariable long userId,
            @RequestParam(defaultValue = "UNVERIFIED") String status
    ) {
        String response = adminServiceFacade.userService.unVerifyUser(userId, status);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }

    @PostMapping("/verifyVendor/{userId}")
    public ResponseEntity<?> verifyVendor(
            @PathVariable long userId,
            @RequestParam(defaultValue = "VERIFIED") String status
    ) {
        String response = adminServiceFacade.userService.verifyVendor(userId, status);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }

    @GetMapping("/getSpecificUserHotelBooking/{userId}")
    public ResponseEntity<?> getSpecificUserHotelBooking(
            @PathVariable long userId,
            @ModelAttribute PaginationRequestDTO paginationRequestDTO
    ) {
        List<BookingDTO> bookings = adminServiceFacade.userService.getSpecificUserHotelBooking(userId, paginationRequestDTO.getPageNumber(), paginationRequestDTO.getPageSize());
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", bookings));
    }
}