package com.fyp.hotel.controller.user;

import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.userDto.BookDto;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.dto.vendorDto.RoomDto;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;
import com.fyp.hotel.util.ValueMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) 
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserServiceImplementation userServiceImplementation;
    @Autowired
    private ValueMapper valueMapper;

    @GetMapping("/profile")
    public String userProfile() {
        return "User Profile";
    }

    @GetMapping("/hotel")
    public ResponseEntity<?> userHotel(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        try {
            List<Hotel> hotelDetails = userServiceImplementation.getAllHotels(page, size);
            List<HotelDto> hotelDTOs = valueMapper.mapToHotelDTOs(hotelDetails);
            System.out.println("hotel details: from controller " + hotelDTOs);

            ApiResponse<List<HotelDto>> response = new ApiResponse<List<HotelDto>>(200, "Success", hotelDTOs);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            // Handle the exception and return an appropriate response
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    //update user profile
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUserDetails(
            @RequestParam(name = "userName") String userName,
            @RequestParam(name = "userFirstName") String userFirstName,
            @RequestParam(name = "userLastName") String userLastName,
            @RequestParam(name = "userEmail") String userEmail,
            @RequestParam(name = "userPhone") String userPhone,
            @RequestParam(name = "userAddress") String userAddress,
            @RequestParam(name = "dateOfBirth") String dateOfBirth
    ) {
        try {
            String response = userServiceImplementation.updateDetails(userName, userFirstName, userLastName, userEmail, userPhone, userAddress, dateOfBirth);
            if ("User details updated successfully".equals(response)) {
                ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
                return ResponseEntity.status(200).body(successResponse);
            } else {
                ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", response);
                return ResponseEntity.status(500).body(errorResponse);
            }
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(){
        String response = userServiceImplementation.clearOutJwtToken();
        if ("User logged out successfully".equals(response)) {
            ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.status(200).body(successResponse);
        } else {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", response);
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    //GET all the hotel rooms of a specific hotel
    @GetMapping("/hotelRooms/{hotelId}")
    public ResponseEntity<?> getHotelRooms(
            @PathVariable(name = "hotelId") Long hotelId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        System.out.println("hotel id: " + hotelId);
        try {
            List<HotelRoom> hotelRooms = userServiceImplementation.getAllRoomsOfHotel(hotelId, page, size);
//            List<RoomDto> hotelRoomDTOS = valueMapper.mapToHotelRoom(hotelRooms);
//
//            System.out.println("hotel details: from controller " + hotelRoomDTOS);
            System.out.println("hotel details: from controller " + hotelRooms);

            ApiResponse<List<HotelRoom>> response = new ApiResponse<List<HotelRoom>>(200, "Success", hotelRooms);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            // Handle the exception and return an appropriate response
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    @GetMapping("/view")
    public ResponseEntity<?> viewUserDetails(){
          return ResponseEntity.ok(userServiceImplementation.getUserById());
    }

    //payment gateway for booking room of a hotel
    @PostMapping("/payment/{roomId}")
    public ResponseEntity<?> paymentGateway(
            @PathVariable(name = "roomId") Long roomId,
            @Validated @RequestParam(name = "checkInDate") String checkInDate,
            @Validated @RequestParam(name = "checkOutDate") String checkOutDate,
            @Validated @RequestParam(name = "daysOfStay") String daysOfStay,
            @Validated @RequestParam(name = "paymentMethod") String paymentMethod
    ) {
        try {

            System.out.println("step 1");
            BookDto bookDto = valueMapper.mapToBooking(roomId, checkInDate, checkOutDate, daysOfStay, paymentMethod);
            System.out.println("step 2");
            String response = userServiceImplementation.hotelPaymentGateWay(bookDto);
            if ("Payment successful by cash on arrival".equals(response) || "Payment successful by khalti".equals(response)) {
                ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
                return ResponseEntity.status(200).body(successResponse);
            } else {
                ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", response);
                return ResponseEntity.status(500).body(errorResponse);
            }
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

}
