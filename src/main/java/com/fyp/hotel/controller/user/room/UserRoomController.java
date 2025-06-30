package com.fyp.hotel.controller.user.room;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.room.CheckRoomAvailabilityDto;
import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.service.user.UserServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserRoomController {

    @Autowired
    private UserServiceFacade userServiceFacade;

    @GetMapping("/hotelRooms/{hotelId}")
    public ResponseEntity<?> getHotelRooms(
            @PathVariable Long hotelId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        List<HotelRoom> hotelRooms = this.userServiceFacade.userHotelService.getAllRoomsOfHotel(hotelId, page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", hotelRooms));
    }

    @GetMapping("/checkRoomAvailability/{roomId}")
    public ResponseEntity<?> checkRoomAvailability(@PathVariable Long roomId) {
        CheckRoomAvailabilityDto availability = this.userServiceFacade.userBookingService.checkRoomAvailability(roomId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", availability));
    }
}