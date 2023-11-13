package com.fyp.hotel.controller.user;

import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


}
