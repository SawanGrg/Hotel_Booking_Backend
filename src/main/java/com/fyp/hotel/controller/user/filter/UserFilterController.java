package com.fyp.hotel.controller.user.filter;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserFilterController {

    private UserService userService;

    @Autowired
    public UserFilterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/filterRooms")
    public ResponseEntity<?> filterRooms(
            @RequestParam Long hotelId,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) String roomCategory,
            @RequestParam(required = false) String roomBedType,
            @RequestParam(required = false) String minRoomPrice,
            @RequestParam(required = false) String maxRoomPrice,
            @RequestParam(required = false) Boolean hasAC,
            @RequestParam(required = false) Boolean hasBalcony,
            @RequestParam(required = false) Boolean hasRefridge,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        List<HotelRoom> rooms = userService.filterRooms(
                hotelId,
                roomType,
                roomCategory,
                roomBedType,
                minRoomPrice,
                maxRoomPrice,
                hasAC,
                hasBalcony,
                hasRefridge,
                page, size
        );
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", rooms));
    }
}