package com.fyp.hotel.controller.user.hotel;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.hotel.HotelDto;
import com.fyp.hotel.dto.hotel.DisplayHotelWithAmenitiesDto;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.service.user.UserServiceFacade;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserHotelController {

    private final ValueMapper valueMapper;
    private UserServiceFacade userServiceFacade;

    @Autowired
    public UserHotelController(UserServiceFacade userServiceFacade, ValueMapper valueMapper) {
        this.userServiceFacade = userServiceFacade;
        this.valueMapper = valueMapper;
    }

    @GetMapping("/hotel")
    public ResponseEntity<?> getAllHotelsWithAmenities(
            @RequestParam(required = false) String hotelName,
            @RequestParam(required = false) String hotelLocation,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<DisplayHotelWithAmenitiesDto> hotels = this.userServiceFacade.userHotelService.getAllHotelsWithAmenities(hotelName, hotelLocation, page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", hotels));
    }

    @GetMapping("/searchHotel")
    public ResponseEntity<?> dynamicSearch(
            @RequestParam(name = "hotelName", required = false) String hotelName,
            @RequestParam(name = "hotelLocation", required = false) String hotelLocation,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        if(hotelName.isEmpty() && hotelLocation.isEmpty()) {
            List<Hotel> hotelDetails = this.userServiceFacade.userHotelService.getAllHotels(page, size);
            List<HotelDto> hotelDTOs = valueMapper.mapToHotelDTOs(hotelDetails);
            ApiResponse<List<HotelDto>> response = new ApiResponse<>(200, "Success", hotelDTOs);
            return ResponseEntity.status(200).body(response);
        } else {
            List<Hotel> hotelDetails = this.userServiceFacade.userHotelService.getHotelBasedOnNameOrLocation(hotelName, hotelLocation, page, size);
            List<HotelDto> hotelDTOs = valueMapper.mapToHotelDTOs(hotelDetails);
            ApiResponse<List<HotelDto>> response = new ApiResponse<>(200, "Success", hotelDTOs);
            return ResponseEntity.status(200).body(response);
        }
    }

    @GetMapping("/hotelUserName/{hotelId}")
    public ResponseEntity<?> getHotelUserName(@PathVariable long hotelId) {
        String userName = this.userServiceFacade.userHotelService.getUserNameOfHotel(hotelId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", userName));
    }
}