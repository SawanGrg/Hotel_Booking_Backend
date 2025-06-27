package com.fyp.hotel.controller.vendor;

import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.userDto.BookingStatusDTO;
import com.fyp.hotel.dto.vendorDto.*;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.model.Report;
import com.fyp.hotel.util.ValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.service.vendor.vendorImpl.VendorServiceImplementation;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) // maxage used to cache where the browser will not send the preflight request
@RequestMapping("/v1/vendor")
public class VendorController {

    @Autowired
    private final ObjectMapper objectMapper;
    @Autowired
    private final VendorServiceImplementation vendorServiceImplementation;
    @Autowired
    private ValueMapper valueMapper;

    private static final Logger logger = LoggerFactory.getLogger(VendorController.class);

    public VendorController(ObjectMapper objectMapper,
                            VendorServiceImplementation vendorServiceImplementation) {
        this.objectMapper = objectMapper;
        this.vendorServiceImplementation = vendorServiceImplementation;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "Vendor dashboard";
    }


    @PostMapping("/addHotelRooms")
    public ResponseEntity<ApiResponse<String>> addHotelRooms(
            @RequestParam("roomData") String stringRoomData,
            @RequestParam("mainRoomImage") MultipartFile stringHotelImage,
            @RequestParam(value = "roomImage1", required = false) MultipartFile stringHotelImage1,
            @RequestParam(value = "roomImage2", required = false) MultipartFile stringHotelImage2,
            @RequestParam(value = "roomImage3", required = false) MultipartFile stringHotelImage3
    ) {
        try {
            RoomDto roomDto = this.objectMapper.readValue(stringRoomData, RoomDto.class);

            if (vendorServiceImplementation.checkRoomExistsOrNot(roomDto.getRoomNumber())) {
                ApiResponse<String> apiResponse = new ApiResponse<>(400, "Room Number already exists", "Room Number already exists");
                return ResponseEntity.badRequest().body(apiResponse);
            }

            String roomStatus = this.vendorServiceImplementation.addRooms(roomDto, stringHotelImage, stringHotelImage1, stringHotelImage2, stringHotelImage3);

            if ("Room Registered Successfully".equals(roomStatus)) {
                logger.info("Room Registered Successfully");
                ApiResponse<String> apiResponse = new ApiResponse<>(200, "Success", roomStatus);
                return ResponseEntity.ok(apiResponse);
            } else {
                logger.error("Failed to add room: {}", roomStatus);
                ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", roomStatus);
                return ResponseEntity.badRequest().body(apiResponse);
            }
        } catch (Exception e) {
            logger.error("Error in vendor controller : {}", e.getMessage());
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @GetMapping("/hotelRooms")
    public ResponseEntity<Page<HotelRoom>> getHotelRooms(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        try {
            Page<HotelRoom> hotelRooms = vendorServiceImplementation.getHotelRooms(page, size);
            return ResponseEntity.ok(hotelRooms);
        } catch (Exception e) {
            logger.error("Error fetching hotel rooms: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/updateRoom/{roomId}")
    public ResponseEntity<?> updateRoom(
            @PathVariable(name = "roomId") Long roomlId,
            @RequestParam("roomData") String stringRoomData,
            @RequestParam("mainRoomImage") MultipartFile stringHotelImage,
            @RequestParam(value = "roomImage1", required = false) MultipartFile stringHotelImage1,
            @RequestParam(value = "roomImage2", required = false) MultipartFile stringHotelImage2,
            @RequestParam(value = "roomImage3", required = false) MultipartFile stringHotelImage3
    ) {
        try {
            logger.info("Attempting to update room with ID: {}", roomlId);
            RoomDto roomDto = this.objectMapper.readValue(stringRoomData, RoomDto.class);
            String response = vendorServiceImplementation.updateRoom(roomlId, roomDto, stringHotelImage, stringHotelImage1, stringHotelImage2, stringHotelImage3);

            if ("Room updated successfully".equals(response)) {
                logger.info("Room updated successfully with ID: {}", roomlId);
                ApiResponse<String> apiResponse = new ApiResponse<>(200, "Success", response);
                return ResponseEntity.ok(apiResponse);
            } else {
                logger.error("Failed to update room with ID {}: {}", roomlId, response);
                ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", response);
                return ResponseEntity.status(500).body(apiResponse);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while updating room with ID {}: {}", roomlId, e.getMessage());
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    @PostMapping("/report")
    public ResponseEntity<?> postReport(
            @RequestBody ReportDto reportDto
    ) {
        try {
            Report report = valueMapper.conversionToReport(reportDto);
            String response = vendorServiceImplementation.postReport(report);
            if ("Report posted successfully".equals(response)) {
                ApiResponse<String> apiResponse = new ApiResponse<>(200, "Success", response);
                return ResponseEntity.ok(apiResponse);
            } else {
                ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", response);
                return ResponseEntity.status(500).body(apiResponse);
            }
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    @GetMapping("/hotelDetails")
    public ResponseEntity<?> getHotelDetails() {
        try {
            Hotel hotel = vendorServiceImplementation.getHotelDetailsService();
            ApiResponse<Hotel> apiResponse = new ApiResponse<>(200, "Success", hotel);
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<?> getBookings(
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "30") int size
    ) {
        try {
            List<VendorBookingDTO> vendorBookingDTOS = vendorServiceImplementation.getBookings(status, page, size);
            ApiResponse<List<VendorBookingDTO>> apiResponse = new ApiResponse<>(200, "Success", vendorBookingDTOS);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    @GetMapping("/analytics")
    public ResponseEntity<?> getVendorAnalytics() {
        try {
            VendorDashboardAnalyticsDTO vendorDashboardAnalyticsDTO = vendorServiceImplementation.getVendorAnalyticsService();
            ApiResponse<VendorDashboardAnalyticsDTO> apiResponse = new ApiResponse<>(200, "Success", vendorDashboardAnalyticsDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    //get the hotel review
    @GetMapping("/hotelReview")
    public ResponseEntity<?> getHotelReview() {
        try {
            List<ReviewDTO> reviews = vendorServiceImplementation.getHotelReviews();
            ApiResponse<List<ReviewDTO>> apiResponse = new ApiResponse<>(200, "Success", reviews);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    //get total revenue from cash on arrival and khalti
    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue() {
        try {
            VendorRevenueDTO revenueDTO = vendorServiceImplementation.getVendorRevenue();
            ApiResponse<VendorRevenueDTO> apiResponse = new ApiResponse<>(200, "Success", revenueDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    //view specific hotel room
    @GetMapping("/viewRoom/{roomId}")
    public ResponseEntity<?> viewRoom(
            @PathVariable(name = "roomId") Long roomId
    ) {
        try {
            List<HotelRoom> hotelRoom = vendorServiceImplementation.getRoomDetails(roomId);
            ApiResponse<List<HotelRoom>> apiResponse = new ApiResponse<>(200, "Success", hotelRoom);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    //get booking status of specific room
    @GetMapping("/bookingStatus/{roomId}")
    public ResponseEntity<?> getBookingStatus(
            @PathVariable(name = "roomId") long roomId
    ) {
        try {
            List<BookingStatusDTO> bookingStatusDTOS = vendorServiceImplementation.getBookingStatusDetails(roomId);
            ApiResponse<List<BookingStatusDTO>> apiResponse = new ApiResponse<>(200, "Success", bookingStatusDTOS);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    //get all the booking details of the specific room
    @GetMapping("/roomHistory/{roomId}")
    public ResponseEntity<?> getRoomHistory(
            @PathVariable(name = "roomId") long roomId
    ) {
        try {
            List<RoomHistoryDTO> bookingStatusDTOS = vendorServiceImplementation.getRoomHistory(roomId);
            ApiResponse<List<RoomHistoryDTO>> apiResponse = new ApiResponse<>(200, "Success", bookingStatusDTOS);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    @PostMapping("/updateHotelDetails")
    public ResponseEntity<?> updateHotelDetails(
            @RequestParam("hotelData") String hotelDataJson,
            @RequestParam(value = "mainHotelImage", required = false) MultipartFile mainHotelImage
    ) {
        try {
            // Deserialize JSON string to HotelDto object
            HotelDto hotelDtoDetails = null;
            if (hotelDataJson != null) {
                hotelDtoDetails = objectMapper.readValue(hotelDataJson, HotelDto.class);
            }
            // Get the original filename of the main hotel image
            String hotelMainImage = mainHotelImage != null ? mainHotelImage.getOriginalFilename() : null;

            // Call service method to update hotel details
            String response = vendorServiceImplementation.updateHotelDetails(
                    hotelDtoDetails != null ? hotelDtoDetails.getHotelName() : null,
                    hotelDtoDetails != null ? hotelDtoDetails.getHotelAddress() : null,
                    hotelDtoDetails != null ? hotelDtoDetails.getHotelContact() : null,
                    hotelDtoDetails != null ? hotelDtoDetails.getHotelEmail() : null,
                    hotelDtoDetails != null ? hotelDtoDetails.getHotelPan() : null,
                    hotelDtoDetails != null ? hotelDtoDetails.getHotelDescription() : null,
                    hotelDtoDetails != null ? hotelDtoDetails.getHotelStar() : 0,
                    mainHotelImage != null ? mainHotelImage : null
            );

            // Return appropriate response based on the service method result
            if ("Hotel details updated successfully".equals(response)) {
                return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
            } else {
                return ResponseEntity.status(500).body(new ApiResponse<>(500, "An error occurred", response));
            }
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Internal Server Error", e.getMessage()));
        }
    }

}

