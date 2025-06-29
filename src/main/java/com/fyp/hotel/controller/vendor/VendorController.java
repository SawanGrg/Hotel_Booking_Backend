package com.fyp.hotel.controller.vendor;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.hotel.HotelDto;
import com.fyp.hotel.dto.hotel.ReviewDTO;
import com.fyp.hotel.dto.room.RoomDto;
import com.fyp.hotel.dto.room.RoomHistoryDTO;
import com.fyp.hotel.dto.booking.BookingStatusDTO;
import com.fyp.hotel.dto.vendorDto.*;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.model.Report;
import com.fyp.hotel.util.ValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.service.vendor.vendorImpl.VendorServiceImplementation;

import java.util.List;

@RestController
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
    ) throws Exception {
        RoomDto roomDto = this.objectMapper.readValue(stringRoomData, RoomDto.class);

        if (vendorServiceImplementation.checkRoomExistsOrNot(roomDto.getRoomNumber())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(400, "Room Number already exists", "Room Number already exists"));
        }

        String roomStatus = this.vendorServiceImplementation.addRooms(roomDto, stringHotelImage, stringHotelImage1, stringHotelImage2, stringHotelImage3);

        if ("Room Registered Successfully".equals(roomStatus)) {
            logger.info("Room Registered Successfully");
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", roomStatus));
        }

        logger.error("Failed to add room: {}", roomStatus);
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(500, "Failed", roomStatus));
    }

    @GetMapping("/hotelRooms")
    public ResponseEntity<Page<HotelRoom>> getHotelRooms(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        Page<HotelRoom> hotelRooms = vendorServiceImplementation.getHotelRooms(page, size);
        return ResponseEntity.ok(hotelRooms);
    }

    @PostMapping("/updateRoom/{roomId}")
    public ResponseEntity<?> updateRoom(
            @PathVariable(name = "roomId") Long roomId,
            @RequestParam("roomData") String stringRoomData,
            @RequestParam("mainRoomImage") MultipartFile stringHotelImage,
            @RequestParam(value = "roomImage1", required = false) MultipartFile stringHotelImage1,
            @RequestParam(value = "roomImage2", required = false) MultipartFile stringHotelImage2,
            @RequestParam(value = "roomImage3", required = false) MultipartFile stringHotelImage3
    ) throws Exception {
        logger.info("Attempting to update room with ID: {}", roomId);
        RoomDto roomDto = this.objectMapper.readValue(stringRoomData, RoomDto.class);
        String response = vendorServiceImplementation.updateRoom(roomId, roomDto, stringHotelImage, stringHotelImage1, stringHotelImage2, stringHotelImage3);

        if ("Room updated successfully".equals(response)) {
            logger.info("Room updated successfully with ID: {}", roomId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
        }

        logger.error("Failed to update room with ID {}: {}", roomId, response);
        return ResponseEntity.status(500)
                .body(new ApiResponse<>(500, "Failed", response));
    }

    @PostMapping("/report")
    public ResponseEntity<?> postReport(@RequestBody ReportDto reportDto) {
        Report report = valueMapper.conversionToReport(reportDto);
        String response = vendorServiceImplementation.postReport(report);

        if ("Report posted successfully".equals(response)) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
        }

        return ResponseEntity.status(500)
                .body(new ApiResponse<>(500, "Failed", response));
    }

    @GetMapping("/hotelDetails")
    public ResponseEntity<?> getHotelDetails() {
        Hotel hotel = vendorServiceImplementation.getHotelDetailsService();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", hotel));
    }

    @GetMapping("/bookings")
    public ResponseEntity<?> getBookings(
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "30") int size
    ) {
        List<VendorBookingDTO> vendorBookingDTOS = vendorServiceImplementation.getBookings(status, page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", vendorBookingDTOS));
    }

    @GetMapping("/analytics")
    public ResponseEntity<?> getVendorAnalytics() {
        VendorDashboardAnalyticsDTO vendorDashboardAnalyticsDTO = vendorServiceImplementation.getVendorAnalyticsService();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", vendorDashboardAnalyticsDTO));
    }

    @GetMapping("/hotelReview")
    public ResponseEntity<?> getHotelReview() {
        List<ReviewDTO> reviews = vendorServiceImplementation.getHotelReviews();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", reviews));
    }

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue() {
        VendorRevenueDTO revenueDTO = vendorServiceImplementation.getVendorRevenue();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", revenueDTO));
    }

    @GetMapping("/viewRoom/{roomId}")
    public ResponseEntity<?> viewRoom(@PathVariable(name = "roomId") Long roomId) {
        List<HotelRoom> hotelRoom = vendorServiceImplementation.getRoomDetails(roomId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", hotelRoom));
    }

    @GetMapping("/bookingStatus/{roomId}")
    public ResponseEntity<?> getBookingStatus(@PathVariable(name = "roomId") long roomId) {
        List<BookingStatusDTO> bookingStatusDTOS = vendorServiceImplementation.getBookingStatusDetails(roomId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", bookingStatusDTOS));
    }

    @GetMapping("/roomHistory/{roomId}")
    public ResponseEntity<?> getRoomHistory(@PathVariable(name = "roomId") long roomId) {
        List<RoomHistoryDTO> bookingStatusDTOS = vendorServiceImplementation.getRoomHistory(roomId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", bookingStatusDTOS));
    }

    @PostMapping("/updateHotelDetails")
    public ResponseEntity<?> updateHotelDetails(
            @RequestParam("hotelData") String hotelDataJson,
            @RequestParam(value = "mainHotelImage", required = false) MultipartFile mainHotelImage
    ) throws Exception {
        HotelDto hotelDtoDetails = objectMapper.readValue(hotelDataJson, HotelDto.class);
        String response = vendorServiceImplementation.updateHotelDetails(
                hotelDtoDetails.getHotelName(),
                hotelDtoDetails.getHotelAddress(),
                hotelDtoDetails.getHotelContact(),
                hotelDtoDetails.getHotelEmail(),
                hotelDtoDetails.getHotelPan(),
                hotelDtoDetails.getHotelDescription(),
                hotelDtoDetails.getHotelStar(),
                mainHotelImage
        );

        if ("Hotel details updated successfully".equals(response)) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
        }

        return ResponseEntity.status(500)
                .body(new ApiResponse<>(500, "An error occurred", response));
    }
}