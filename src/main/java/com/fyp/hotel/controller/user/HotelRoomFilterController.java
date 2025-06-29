package com.fyp.hotel.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.user.*;
import com.fyp.hotel.model.*;
import com.fyp.hotel.service.user.userImpl.UserServiceImpl;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class HotelRoomFilterController {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private  UserProfileDto userProfileDto;
    @Autowired
    private ValueMapper valueMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/filterRooms")
    public ResponseEntity<?> filterRoom(
            @RequestParam(name = "hotelId", required = true) Long hotelId,
            @RequestParam(name = "roomType", required = false) String roomType,
            @RequestParam(name = "roomCategory", required = false) String roomCategory,
            @RequestParam(name = "bedType", required = false) String roomBedType,
            @RequestParam(name = "minRoomPrice", required = false) String minRoomPrice,
            @RequestParam(name = "maxRoomPrice", required = false) String maxRoomPrice,
            @RequestParam(name = "hasAC", required = false) Boolean hasAC,
            @RequestParam(name = "hasBalcony", required = false) Boolean hasBalcony,
            @RequestParam(name = "hasRefridge", required = false) Boolean hasRefridge,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size
    ) {
            if(roomType != null && roomType.isEmpty()){
                roomType = null;
            }
            if(roomCategory != null && roomCategory.isEmpty()){
                roomCategory = null;
            }
            if(roomBedType != null && roomBedType.isEmpty()){
                roomBedType = null;
            }

            List<HotelRoom> hotelRooms = userServiceImpl.filterRooms(
                    hotelId,
                    roomType,
                    roomCategory,
                    roomBedType,
                    minRoomPrice,
                    maxRoomPrice,
                    hasAC,
                    hasBalcony,
                    hasRefridge,
                    page,
                    size
            );
            ApiResponse<List<HotelRoom>> response = new ApiResponse<>(200, "Success", hotelRooms);
            return ResponseEntity.status(200).body(response);
    }
}
