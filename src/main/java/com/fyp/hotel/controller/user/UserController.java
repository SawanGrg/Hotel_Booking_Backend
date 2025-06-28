package com.fyp.hotel.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.*;
import com.fyp.hotel.dto.khalti.KhaltiResponseDTO;
import com.fyp.hotel.dto.userDto.*;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.model.*;
import com.fyp.hotel.service.user.userImpl.UserServiceImpl;
import com.fyp.hotel.util.ValueMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserProfileDto userProfileDto;
    @Autowired
    private ValueMapper valueMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/hotel")
    public ResponseEntity<?> userHotel(
            @RequestParam(name = "hotelName", required = false, defaultValue = "") String hotelName,
            @RequestParam(name = "hotelLocation", required = false, defaultValue = "") String hotelLocation,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        List<DisplayHotelWithAmenitiesDto> hotelDetails = userServiceImpl.getAllHotelsWithAmenities(hotelName, hotelLocation, page, size);
        ApiResponse<List<DisplayHotelWithAmenitiesDto>> response = new ApiResponse<List<DisplayHotelWithAmenitiesDto>>(200, "Success", hotelDetails);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/update-user-details")
    public ResponseEntity<?> updateUserDetails(
            @RequestParam(value = "userUpgradingDetails", required = false) String userUpgradingDetailsJson,
            @RequestParam(value = "userProfileImage", required = false) MultipartFile userProfileImage
    ) throws Exception
    {
        UserProfileDto userUpgradingDetails = null;
        if (userUpgradingDetailsJson != null) {
            userUpgradingDetails = objectMapper.readValue(userUpgradingDetailsJson, UserProfileDto.class);
        }
        String userProfileImageFilename = userProfileImage != null ? userProfileImage.getOriginalFilename() : null;
        String response = userServiceImpl.updateDetails(
                userUpgradingDetails != null ? userUpgradingDetails.getUserName() : null,
                userUpgradingDetails != null ? userUpgradingDetails.getUserFirstName() : null,
                userUpgradingDetails != null ? userUpgradingDetails.getUserLastName() : null,
                userUpgradingDetails != null ? userUpgradingDetails.getUserEmail() : null,
                userUpgradingDetails != null ? userUpgradingDetails.getUserPhone() : null,
                userUpgradingDetails != null ? userUpgradingDetails.getUserAddress() : null,
                userUpgradingDetails != null ? userUpgradingDetails.getDateOfBirth() : null,
                userProfileImage
        );
        if ("User details updated successfully".equals(response)) {
            ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.status(200).body(successResponse);
        } else {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", response);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/user-change-password")
    public ResponseEntity<?> changePassword(
            @Valid()
            @RequestBody UserChangePasswordDto userChangePasswordDto
    ) {
        String response = userServiceImpl.changePassword(userChangePasswordDto);
        if ("Password changed successfully".equals(response)) {
            ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.status(200).body(successResponse);
        } else {
            ApiResponse<String> errorResponse = new ApiResponse<>(400, "An error occurred", response);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        String response = userServiceImpl.clearOutJwtToken();
        if ("User logged out successfully".equals(response)) {
            ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.status(200).body(successResponse);
        } else {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", response);
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    @GetMapping("/hotelRooms/{hotelId}")
    public ResponseEntity<?> getHotelRooms(
            @PathVariable(name = "hotelId") Long hotelId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        List<HotelRoom> hotelRooms = userServiceImpl.getAllRoomsOfHotel(hotelId, page, size);
        ApiResponse<List<HotelRoom>> response = new ApiResponse<List<HotelRoom>>(200, "Success", hotelRooms);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/bookingStatus/{hotelId}")
    public ResponseEntity<?> getBookingStatus(
            @PathVariable(name = "hotelId") Long hotelId
    ) {
        List<BookingStatusDTO> bookingStatusDtos = userServiceImpl.getBookingStatus(hotelId);
        ApiResponse<List<BookingStatusDTO>> response = new ApiResponse<>(200, "Success", bookingStatusDtos);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/view-user-details")
    public ResponseEntity<?> viewUserDetails() {
        User user = userServiceImpl.getUserById();
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            ApiResponse<User> successResponse = new ApiResponse<>(200, "Success", user);
            return ResponseEntity.status(200).body(successResponse);
        } else {
            ApiResponse<User> errorResponse = new ApiResponse<>(500, "An error occurred hello", user);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/khalti/payment/{roomId}")
    public KhaltiResponseDTO processKhaltiPayment(
            @PathVariable(name = "roomId") Long roomId,
            @RequestParam(name = "checkInDate") String checkInDate,
            @RequestParam(name = "checkOutDate") String checkOutDate,
            @RequestParam(name = "numberOfGuest", required = false, defaultValue = "2") String numberOfGuest,
            @RequestParam(name = "paymentMethod") String paymentMethod
    ) {
        BookDto bookDto = valueMapper.mapToBooking(roomId, checkInDate, checkOutDate, numberOfGuest, paymentMethod);
        return userServiceImpl.ePaymentGateway(bookDto);
    }

    @PostMapping("/khalti/update")
    public ResponseEntity<?> updateKhalti(
            @RequestParam(name = "pidx") String pidx,
            @RequestParam(name = "status") String status,
            @RequestParam(name = "bookingId") String bookingId,
            @RequestParam(name = "totalAmount") long totalAmount
    ) {
        String res = this.userServiceImpl.updatePaymentTable(pidx, status, bookingId, totalAmount);
        if (Objects.equals(res, "SuccessFull enter")) {
            ApiResponse<String> apiResponse = new ApiResponse<>(200, "success", res);
            return ResponseEntity.ok().body(apiResponse);
        } else {
            ApiResponse<String> apiResponse = new ApiResponse<>(400, "failure", "Failed to update payment table");
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @GetMapping("/searchHotel")
    public ResponseEntity<?> dynamicSearch(
            @RequestParam(name = "hotelName", required = false) String hotelName,
            @RequestParam(name = "hotelLocation", required = false) String hotelLocation,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        if(hotelName.isEmpty() && hotelLocation.isEmpty()) {
            System.out.println("hotel name and location are null");
            List<Hotel> hotelDetails = userServiceImpl.getAllHotels(page, size);
            List<HotelDto> hotelDTOs = valueMapper.mapToHotelDTOs(hotelDetails);
            ApiResponse<List<HotelDto>> response = new ApiResponse<>(200, "Success", hotelDTOs);
            return ResponseEntity.status(200).body(response);
        } else {
            List<Hotel> hotelDetails = userServiceImpl.getHotelBasedOnNameOrLocation(hotelName, hotelLocation, page, size);
            List<HotelDto> hotelDTOs = valueMapper.mapToHotelDTOs(hotelDetails);
            ApiResponse<List<HotelDto>> response = new ApiResponse<>(200, "Success", hotelDTOs);
            return ResponseEntity.status(200).body(response);
        }
    }

    @GetMapping("/checkRoomAvailability/{roomId}")
    public ResponseEntity<?> checkRoomAvailability(
            @PathVariable(name = "roomId") Long roomId
    ) {
        CheckRoomAvailabilityDto checkRoomAvailabilityDto = userServiceImpl.checkRoomAvailability(roomId);
        ApiResponse<CheckRoomAvailabilityDto> response = new ApiResponse<>(200, "Success", checkRoomAvailabilityDto);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/hotelReview/{hotelId}")
    public ResponseEntity<?> viewHotelReview(
            @PathVariable(name = "hotelId") long hotelId
    ) {
        List<HotelReviewDTO> reviews = userServiceImpl.getAllHotelReviews(hotelId);
        ApiResponse<List<HotelReviewDTO>> response = new ApiResponse<>(200, "Success", reviews);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/viewBlog")
    public ResponseEntity<?> viewBlog() {
        List<BlogDTO> blogs = userServiceImpl.viewUserBlog();
        ApiResponse<List<BlogDTO>> response = new ApiResponse<>(200, "Success", blogs);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/viewBlog/{blogId}")
    public ResponseEntity<?> getSpecificBlog(
            @PathVariable(name = "blogId") long blogId
    ) {
        BlogDTO blog = userServiceImpl.getSpecificBlog(blogId);
        ApiResponse<BlogDTO> response = new ApiResponse<>(200, "Success", blog);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/postBlogComment/{blogId}")
    public ResponseEntity<?> postBlogComment(
            @PathVariable(name = "blogId") long blogId,
            @RequestBody BlogCommentDTO blogCommentDTO
    ) {
        String response = userServiceImpl.postBlogComment(blogId, blogCommentDTO);
        ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
        return ResponseEntity.status(200).body(successResponse);
    }

    @GetMapping("/viewPostBlogComment/{blogId}")
    public ResponseEntity<?> viewBlogComment(
            @PathVariable(name = "blogId") long blogId
    ) {
        List<BlogCommentDTO> blogCommentDTOs = this.userServiceImpl.getAllBlogCommentSpecificBlog(blogId);
        ApiResponse<List<BlogCommentDTO>> blogCommentResponse = new ApiResponse<>(200, "success", blogCommentDTOs);
        return ResponseEntity.status(200).body(blogCommentResponse);
    }

    @GetMapping("/viewBookingDetails")
    public ResponseEntity<?> viewBookingDetails() {
        List<BookingDTO> bookingDtos = userServiceImpl.viewBookingDetails();
        ApiResponse<List<BookingDTO>> response = new ApiResponse<>(200, "Success", bookingDtos);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/hotelUserName/{hotelId}")
    public ResponseEntity<?> getHotelUserName(
            @PathVariable(name = "hotelId") long hotelId
    ) {
        String hotelUserName = userServiceImpl.getUserNameOfHotel(hotelId);
        ApiResponse<String> response = new ApiResponse<>(200, "Success", hotelUserName);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/getInTouch")
    public ResponseEntity<?> getInTouch(
            @RequestBody UserMessageDTO userMessageDTO
    ) {
        String response = userServiceImpl.postUserMessage(userMessageDTO);
        ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
        return ResponseEntity.status(200).body(successResponse);
    }
}