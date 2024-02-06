package com.fyp.hotel.controller.user;

import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.userDto.BookDto;
import com.fyp.hotel.dto.userDto.UserChangePasswordDto;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.dto.vendorDto.RoomDto;
import com.fyp.hotel.model.*;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;
import com.fyp.hotel.util.ValueMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam(name = "dateOfBirth") String dateOfBirth,
            @RequestParam(name = "userProfileImage") MultipartFile userProfileImage
    ) {
        try {
            String response = userServiceImplementation.updateDetails(userName, userFirstName, userLastName, userEmail, userPhone, userAddress, dateOfBirth, userProfileImage);
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

    //change possword
    @PostMapping("/user-change-password")
    public ResponseEntity<?> changePassword(
            @Valid()
            @RequestBody UserChangePasswordDto userChangePasswordDto
            )
    {
        try {

            System.out.println("userChangePasswordDto: " + userChangePasswordDto.getNewPassword());
            System.out.println("userChangePasswordDto: " + userChangePasswordDto.getOldPassword());

            String response = userServiceImplementation.changePassword(userChangePasswordDto);
            if ("Password changed successfully".equals(response)) {
                ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
                return ResponseEntity.status(200).body(successResponse);
            } else {
                ApiResponse<String> errorResponse = new ApiResponse<>(400, "An error occurred", response);
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
    @GetMapping("/view-user-details")
    public ResponseEntity<?> viewUserDetails(){
try {
            User user = userServiceImplementation.getUserById();
    if (user.getUsername() != null && !user.getUsername().isEmpty()) {
        // Username is not empty or null
        ApiResponse<User> successResponse = new ApiResponse<>(200, "Success", user);
        return ResponseEntity.status(200).body(successResponse);
    } else {
        // Username is empty or null
        ApiResponse<User> errorResponse = new ApiResponse<>(500, "An error occurred hello", user);
        return ResponseEntity.status(500).body(errorResponse);
    }

        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    //payment gateway for booking room of a hotel
    @PostMapping("/payment/{roomId}")
    public ResponseEntity<?> paymentGateway(
            @PathVariable(name = "roomId") Long roomId,
            @RequestParam(name = "checkInDate") String checkInDate,
            @RequestParam(name = "checkOutDate") String checkOutDate,
//            @Validated @RequestParam(name = "daysOfStay") String daysOfStay,
            @RequestParam(name = "numberOfGuest", required = false, defaultValue = "2") String numberOfGuest,
            @RequestParam(name = "paymentMethod") String paymentMethod
    ) {
        try {

            System.out.println("step 1");
            BookDto bookDto = valueMapper.mapToBooking(roomId, checkInDate, checkOutDate, numberOfGuest, paymentMethod);
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

    //filter controller
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
        System.out.println("hotel id: " + hotelId);
        System.out.println("room type: " + roomType);
        System.out.println("room category: " + roomCategory);
        System.out.println("room bed type: " + roomBedType);
        System.out.println("min room price: " + minRoomPrice);
        System.out.println("max room price: " + maxRoomPrice);
        System.out.println("has AC: " + hasAC);
        System.out.println("has balcony: " + hasBalcony);
        System.out.println("has fridge: " + hasRefridge);
        System.out.println("page: " + page);
        System.out.println("size: " + size);
        try {

            if(roomType != null && roomType.isEmpty()){
                roomType = null;
            }
            if(roomCategory != null && roomCategory.isEmpty()){
                roomCategory = null;
            }
            if(roomBedType != null && roomBedType.isEmpty()){
                roomBedType = null;
            }

            List<HotelRoom> hotelRooms = userServiceImplementation.filterRooms(
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

            System.out.println("hotel details: from controller " + hotelRooms);

            ApiResponse<List<HotelRoom>> response = new ApiResponse<>(200, "Success", hotelRooms);
            return ResponseEntity.status(200).body(response);
        }
        catch (Exception e) {
            // Handle other exceptions and return an appropriate response
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred in filter spring boot", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }

    }



}
