package com.fyp.hotel.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.*;
import com.fyp.hotel.dto.khalti.KhaltiResponseDTO;
import com.fyp.hotel.dto.userDto.*;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.dto.vendorDto.RoomDto;
import com.fyp.hotel.model.*;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;
import com.fyp.hotel.util.ValueMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) 
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserServiceImplementation userServiceImplementation;
    @Autowired
    private  UserProfileDto userProfileDto;
    @Autowired
    private ValueMapper valueMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/profile")
    public String userProfile() {
        return "User Profile";
    }

    @GetMapping("/hotel")
    public ResponseEntity<?> userHotel(
            @RequestParam(name = "hotelName", required = false, defaultValue = "") String hotelName,
            @RequestParam(name = "hotelLocation", required = false, defaultValue = "") String hotelLocation,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        try {
            List<DisplayHotelWithAmenitiesDto> hotelDetails = userServiceImplementation.getAllHotelsWithAmenities(hotelName, hotelLocation, page, size);

            ApiResponse<List<DisplayHotelWithAmenitiesDto>> response = new ApiResponse<List<DisplayHotelWithAmenitiesDto>>(200, "Success", hotelDetails);
            System.out.println(" new hotel details: from controller  ---------->" + hotelDetails);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            // Handle the exception and return an appropriate response
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    //update user profile
    @PostMapping("/update-user-details")
    public ResponseEntity<?> updateUserDetails(
            @RequestParam(value = "userUpgradingDetails", required = false) String userUpgradingDetailsJson,
            @RequestParam(value = "userProfileImage", required = false) MultipartFile userProfileImage
    ) {
        try {
            UserProfileDto userUpgradingDetails = null;
            if (userUpgradingDetailsJson != null) {
                userUpgradingDetails = objectMapper.readValue(userUpgradingDetailsJson, UserProfileDto.class);
            }

            String userProfileImageFilename = userProfileImage != null ? userProfileImage.getOriginalFilename() : null;

            // Log userUpgradingDetails and userProfileImageFilename
            System.out.println("user name : " + userUpgradingDetails.getUserName());
            System.out.println("user first name : " + userUpgradingDetails.getUserFirstName());
            System.out.println("user last name : " + userUpgradingDetails.getUserLastName());
            System.out.println("user email : " + userUpgradingDetails.getUserEmail());
            System.out.println("user phone : " + userUpgradingDetails.getUserPhone());
            System.out.println("user address : " + userUpgradingDetails.getUserAddress());
            System.out.println("user date of birth : " + userUpgradingDetails.getDateOfBirth());
            System.out.println("user profile image : " + userProfileImageFilename);

            System.out.println("before calling the service method step 1");
            // Call the service method to update user details, passing null for userProfileImage if not provided
            String response = userServiceImplementation.updateDetails(
                    userUpgradingDetails != null ? userUpgradingDetails.getUserName() : null,
                    userUpgradingDetails != null ? userUpgradingDetails.getUserFirstName() : null,
                    userUpgradingDetails != null ? userUpgradingDetails.getUserLastName() : null,
                    userUpgradingDetails != null ? userUpgradingDetails.getUserEmail() : null,
                    userUpgradingDetails != null ? userUpgradingDetails.getUserPhone() : null,
                    userUpgradingDetails != null ? userUpgradingDetails.getUserAddress() : null,
                    userUpgradingDetails != null ? userUpgradingDetails.getDateOfBirth() : null,
                    userProfileImage // Pass userProfileImage directly without checking for null
            );

            // Return appropriate response based on the service method result
            if ("User details updated successfully".equals(response)) {
                ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
                return ResponseEntity.status(200).body(successResponse);
            } else {
                ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", response);
                return ResponseEntity.status(500).body(errorResponse);
            }
        } catch (Exception e) {
            // Handle other exceptions
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "Internal Server Error", e.getMessage());
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

    //extract booking status from hotel id
    @GetMapping("/bookingStatus/{hotelId}")
    public ResponseEntity<?> getBookingStatus(
            @PathVariable(name = "hotelId") Long hotelId
    ) {
        try {
            List<BookingStatusDTO> bookingStatusDtos = userServiceImplementation.getBookingStatus(hotelId);
            ApiResponse<List<BookingStatusDTO>> response = new ApiResponse<>(200, "Success", bookingStatusDtos);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
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
//    @PostMapping("/payment/{roomId}")
//    public ResponseEntity<?> paymentGateway(
//            @PathVariable(name = "roomId") Long roomId,
//            @RequestParam(name = "checkInDate") String checkInDate,
//            @RequestParam(name = "checkOutDate") String checkOutDate,
////            @Validated @RequestParam(name = "daysOfStay") String daysOfStay,
//            @RequestParam(name = "numberOfGuest", required = false, defaultValue = "2") String numberOfGuest,
//            @RequestParam(name = "paymentMethod") String paymentMethod
//    ) {
//        try {
//
//            System.out.println("step 1");
//            BookDto bookDto = valueMapper.mapToBooking(roomId, checkInDate, checkOutDate, numberOfGuest, paymentMethod);
//            System.out.println("step 2");
//            String response = userServiceImplementation.hotelPaymentGateWay(bookDto);
//            if ("Payment successful by cash on arrival".equals(response) || "Payment successful by khalti".equals(response)) {
//                ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
//                return ResponseEntity.status(200).body(successResponse);
//            } else {
//                ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", response);
//                return ResponseEntity.status(500).body(errorResponse);
//            }
//        } catch (Exception e) {
//            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
//            return ResponseEntity.status(500).body(errorResponse);
//        }
//    }

    @PostMapping("/khalti/payment/{roomId}")
    public KhaltiResponseDTO processKhaltiPayment(
            @PathVariable(name = "roomId") Long roomId,
            @RequestParam(name = "checkInDate") String checkInDate,
            @RequestParam(name = "checkOutDate") String checkOutDate,
            @RequestParam(name = "numberOfGuest", required = false, defaultValue = "2") String numberOfGuest,
            @RequestParam(name = "paymentMethod") String paymentMethod
    ) {
        try {
            // Create a BookDto from the request parameters
            BookDto bookDto = valueMapper.mapToBooking(roomId, checkInDate, checkOutDate, numberOfGuest, paymentMethod);
            // Call the Khalti payment service method
            return userServiceImplementation.ePaymentGateway(bookDto);
        } catch (Exception e) {
            // Handle any exceptions and return an error response
            e.printStackTrace();
            System.out.println("Error occurred in processKhaltiPayment: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //post req where we get the pidx, status, booking id all the details in this khalti req to save in db
    @PostMapping("/khalti/update")
    public ResponseEntity<?> updateKhalti(
            @RequestParam(name = "pidx") String pidx,
            @RequestParam(name = "status") String status,
            @RequestParam(name = "bookingId") String bookingId,
            @RequestParam(name = "totalAmount") long totalAmount
    ){
        try {
            String res = this.userServiceImplementation.updatePaymentTable(pidx, status, bookingId, totalAmount);
            if (Objects.equals(res, "SuccessFull enter")) {
                ApiResponse<String> apiResponse = new ApiResponse<>(200, "success", res);
                return ResponseEntity.ok().body(apiResponse);
            } else {
                // Handle the case where the update operation was not successful
                ApiResponse<String> apiResponse = new ApiResponse<>(400, "failure", "Failed to update payment table");
                return ResponseEntity.badRequest().body(apiResponse);
            }
        } catch (Exception e) {
            // Handle exceptions
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "error", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    //filter controller
//    @GetMapping("/filterRooms")
//    public ResponseEntity<?> filterRoom(
//            @RequestParam(name = "hotelId", required = true) Long hotelId,
//            @RequestParam(name = "roomType", required = false) String roomType,
//            @RequestParam(name = "roomCategory", required = false) String roomCategory,
//            @RequestParam(name = "bedType", required = false) String roomBedType,
//            @RequestParam(name = "minRoomPrice", required = false) String minRoomPrice,
//            @RequestParam(name = "maxRoomPrice", required = false) String maxRoomPrice,
//            @RequestParam(name = "hasAC", required = false) Boolean hasAC,
//            @RequestParam(name = "hasBalcony", required = false) Boolean hasBalcony,
//            @RequestParam(name = "hasRefridge", required = false) Boolean hasRefridge,
//            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
//            @RequestParam(name = "size", required = false, defaultValue = "5") int size
//    ) {
//        System.out.println("hotel id: " + hotelId);
//        System.out.println("room type: " + roomType);
//        System.out.println("room category: " + roomCategory);
//        System.out.println("room bed type: " + roomBedType);
//        System.out.println("min room price: " + minRoomPrice);
//        System.out.println("max room price: " + maxRoomPrice);
//        System.out.println("has AC: " + hasAC);
//        System.out.println("has balcony: " + hasBalcony);
//        System.out.println("has fridge: " + hasRefridge);
//        System.out.println("page: " + page);
//        System.out.println("size: " + size);
//        try {
//
//            if(roomType != null && roomType.isEmpty()){
//                roomType = null;
//            }
//            if(roomCategory != null && roomCategory.isEmpty()){
//                roomCategory = null;
//            }
//            if(roomBedType != null && roomBedType.isEmpty()){
//                roomBedType = null;
//            }
//
//            List<HotelRoom> hotelRooms = userServiceImplementation.filterRooms(
//                    hotelId,
//                    roomType,
//                    roomCategory,
//                    roomBedType,
//                    minRoomPrice,
//                    maxRoomPrice,
//                    hasAC,
//                    hasBalcony,
//                    hasRefridge,
//                    page,
//                    size
//            );
//
//            System.out.println("hotel details: from controller " + hotelRooms);
//
//            ApiResponse<List<HotelRoom>> response = new ApiResponse<>(200, "Success", hotelRooms);
//            return ResponseEntity.status(200).body(response);
//        }
//        catch (Exception e) {
//            // Handle other exceptions and return an appropriate response
//            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred in filter spring boot", e.getMessage());
//            return ResponseEntity.status(500).body(errorResponse);
//        }
//
//    }

    //search based on hotel name or location
    @GetMapping("/searchHotel")
    public ResponseEntity<?> dynamicSearch(
            @RequestParam(name = "hotelName", required = false) String hotelName,
            @RequestParam(name = "hotelLocation", required = false) String hotelLocation,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        System.out.println("hotel name: " + hotelName);
        System.out.println("hotel location: " + hotelLocation);

        if(hotelName.isEmpty() && hotelLocation.isEmpty()){
            System.out.println("hotel name and location are null");
            try {
                List<Hotel> hotelDetails = userServiceImplementation.getAllHotels(page, size);
                List<HotelDto> hotelDTOs = valueMapper.mapToHotelDTOs(hotelDetails);
                System.out.println("hotel details: from controller " + hotelDTOs);

                ApiResponse<List<HotelDto>> response = new ApiResponse<>(200, "Success", hotelDTOs);
                return ResponseEntity.status(200).body(response);
            } catch (Exception e) {
                // Handle the exception and return an appropriate response
                ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
                return ResponseEntity.status(500).body(errorResponse);
            }

        } else {
            try {
                System.out.println("hotel name and location are not null");
                List<Hotel> hotelDetails = userServiceImplementation.getHotelBasedOnNameOrLocation(hotelName, hotelLocation, page, size);
                List<HotelDto> hotelDTOs = valueMapper.mapToHotelDTOs(hotelDetails);
                System.out.println("hotel details: from controller " + hotelDTOs);

                ApiResponse<List<HotelDto>> response = new ApiResponse<>(200, "Success", hotelDTOs);
                return ResponseEntity.status(200).body(response);
            } catch (Exception e) {
                // Handle the exception and return an appropriate response
                ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
                return ResponseEntity.status(500).body(errorResponse);
            }
        }
    }

    @GetMapping("/checkRoomAvailability/{roomId}")
    public ResponseEntity<?> checkRoomAvailability(
            @PathVariable(name = "roomId") Long roomId
    ) {
        try {
            CheckRoomAvailabilityDto checkRoomAvailabilityDto = userServiceImplementation.checkRoomAvailability(roomId);
            ApiResponse<CheckRoomAvailabilityDto> response = new ApiResponse<>(200, "Success", checkRoomAvailabilityDto);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);

        }
    }

    // user post reviews the specific hotel
//    @PostMapping("/review/{hotelId}")
//    public ResponseEntity<?> postHotelReview(
//            @PathVariable(name = "hotelId") long hotelId,
//            @RequestBody HotelReviewDTO hotelReviewDTO
//    ) {
//        try {
//            String response = userServiceImplementation.postHotelReviewByUser(hotelId, hotelReviewDTO);
//
//            if ("Review posted successfully.".equals(response)) { // Check the response message here
//                ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
//                return ResponseEntity.status(200).body(successResponse);
//            } else {
//                ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", response);
//                return ResponseEntity.status(500).body(errorResponse);
//            }
//        } catch (Exception e) {
//            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
//            return ResponseEntity.status(500).body(errorResponse);
//        }
//    }

    //user view all the review of specific hotel
    @GetMapping("/hotelReview/{hotelId}")
    public ResponseEntity<?> viewHotelReview(
            @PathVariable(name = "hotelId") long hotelId
    ) {
        try {
            List<HotelReviewDTO> reviews = userServiceImplementation.getAllHotelReviews(hotelId);
            ApiResponse<List<HotelReviewDTO>> response = new ApiResponse<>(200, "Success", reviews);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    //post blog by user
//    @PostMapping("/postBlog")
//    public ResponseEntity<?> postBlog(
//            @RequestParam("blogImage") MultipartFile blogImage,
//            @RequestParam("blogDTO") String blogDTOJson
//    ) {
//        BlogDTO blogDTO = null;
//        try {
//            blogDTO = objectMapper.readValue(blogDTOJson, BlogDTO.class); // Convert the JSON string to BlogDTO object
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        try {
//            String response = userServiceImplementation.postUserBlog(blogImage, blogDTO);
//            ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
//            return ResponseEntity.status(200).body(successResponse);
//        } catch (Exception e) {
//            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
//            return ResponseEntity.status(500).body(errorResponse);
//        }
//    }


    //view all blog
    @GetMapping("/viewBlog")
    public ResponseEntity<?> viewBlog() {
        try {
            List<BlogDTO> blogs = userServiceImplementation.viewUserBlog();
            ApiResponse<List<BlogDTO>> response = new ApiResponse<>(200, "Success", blogs);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    //view specific blog
    @GetMapping("/viewBlog/{blogId}")
    public ResponseEntity<?> getSpecificBlog(
            @PathVariable(name = "blogId") long blogId
    ){
        try {
            BlogDTO blog = userServiceImplementation.getSpecificBlog(blogId);
            ApiResponse<BlogDTO> response = new ApiResponse<>(200, "Success", blog);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

//    post comments in blog
    @PostMapping("/postBlogComment/{blogId}")
    public ResponseEntity<?> postBlogComment(
            @PathVariable(name = "blogId") long blogId,
            @RequestBody BlogCommentDTO blogCommentDTO
    ) {

        System.out.println("blog id: " + blogId);
        System.out.println("blog comment: " + blogCommentDTO.getComment());
        try {
            String response = userServiceImplementation.postBlogComment(blogId, blogCommentDTO);
            ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.status(200).body(successResponse);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    //get all the blog comment of a specific blog
    @GetMapping("/viewPostBlogComment/{blogId}")
    public ResponseEntity<?> viewBlogComment(
            @PathVariable(name = "blogId") long blogId
    ){
        try {
            List<BlogCommentDTO> blogCommentDTOs = this.userServiceImplementation.getAllBlogCommentSpecificBlog(blogId);
            ApiResponse<List<BlogCommentDTO>> blogCommentResponse = new ApiResponse<>(200, "success" , blogCommentDTOs);
            return ResponseEntity.status(200).body(blogCommentResponse);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(404, "Not found", e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    //get specific user booking details
    @GetMapping("/viewBookingDetails")
    public ResponseEntity<?> viewBookingDetails(){
        try {
            List<BookingDTO> bookingDtos = userServiceImplementation.viewBookingDetails();
            ApiResponse<List<BookingDTO>> response = new ApiResponse<>(200, "Success", bookingDtos);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(404, "Not found", e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @GetMapping("/hotelUserName/{hotelId}")
    public ResponseEntity<?> getHotelUserName(
            @PathVariable(name = "hotelId") long hotelId
    ){
        try {
            String hotelUserName = userServiceImplementation.getUserNameOfHotel(hotelId);
            ApiResponse<String> response = new ApiResponse<>(200, "Success", hotelUserName);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(404, "Not found", e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    //user post message for getting in touch
    @PostMapping("/getInTouch")
    public ResponseEntity<?> getInTouch(
            @RequestBody UserMessageDTO userMessageDTO
    ) {
        try {
            String response = userServiceImplementation.postUserMessage(userMessageDTO);
            ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.status(200).body(successResponse);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }



}
