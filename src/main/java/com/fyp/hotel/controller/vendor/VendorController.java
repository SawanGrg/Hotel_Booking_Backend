package com.fyp.hotel.controller.vendor;

import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.vendorDto.ReportDto;
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
import com.fyp.hotel.dto.vendorDto.RoomDto;
import com.fyp.hotel.serviceImpl.vendor.VendorServiceImplementation;

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
    public String dashboard(){
        return "Vendor dashboard";
    }


    //insert the hotel rooms 
    @PostMapping("/addHotelRooms")
    public ResponseEntity<ApiResponse<String>> addHotelRooms(
        @RequestParam("roomData") String stringRoomData,
        @RequestParam("mainRoomImage") MultipartFile stringHotelImage,
        @RequestParam(value = "roomImage1", required = false) MultipartFile stringHotelImage1,
        @RequestParam(value = "roomImage2",required = false) MultipartFile stringHotelImage2,
        @RequestParam(value = "roomImage3", required = false) MultipartFile stringHotelImage3
    ){
        System.out.println("room Data : " + stringRoomData);
        System.out.println("Hotel Image : " + stringHotelImage);
        System.out.println("Hotel Image 1 : " + stringHotelImage1);
        System.out.println("Hotel Image 2 : " + stringHotelImage2);
        System.out.println("Hotel Image 3 : " + stringHotelImage3);

        try {
            //read the json data and convert it into room dto
            RoomDto roomDto = this.objectMapper.readValue(stringRoomData, RoomDto.class);
            System.out.println("Hotel DTO : " + roomDto);

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
            System.out.println("Error in vendor controller : " + e.getMessage());
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.badRequest().body(apiResponse);
            
        }
    }

    //get all the hotel rooms
    @GetMapping("/hotelRooms")
    public ResponseEntity<Page<HotelRoom>> getHotelRooms(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            Page<HotelRoom> hotelRooms = vendorServiceImplementation.getHotelRooms(page, size);
            return ResponseEntity.ok(hotelRooms);
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //get

    //Update the specific details
    @PostMapping("/update/{hotelId}")
    public ResponseEntity<?> updateRoom(
            @PathVariable(name = "hotelId") Long hotelId,
            @RequestParam("roomData") String stringRoomData,
            @RequestParam("mainRoomImage") MultipartFile stringHotelImage,
            @RequestParam("roomImage1") MultipartFile stringHotelImage1,
            @RequestParam("roomImage2") MultipartFile stringHotelImage2,
            @RequestParam("roomImage3") MultipartFile stringHotelImage3
    ){
        try {
            logger.info("Attempting to update room with ID: {}", hotelId);
            RoomDto roomDto = this.objectMapper.readValue(stringRoomData, RoomDto.class); // Read the JSON data and convert it into RoomDto
            String response = vendorServiceImplementation.updateRoom(hotelId, roomDto, stringHotelImage, stringHotelImage1, stringHotelImage2, stringHotelImage3);

            if ("Room updated successfully".equals(response)) {
                logger.info("Room updated successfully with ID: {}", hotelId);
                return ResponseEntity.ok(response);
            } else {
                logger.error("Failed to update room with ID {}: {}", hotelId, response);
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while updating room with ID {}: {}", hotelId, e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    //delete a specific room
    @PostMapping("/delete/{hotelId}")
    public ResponseEntity<?> deleteRoom(
            @PathVariable(name = "hotelId") Long hotelId
    ){
        try {
            logger.info("Attempting to delete room with ID: {}", hotelId);
            String response = vendorServiceImplementation.deleteRoom(hotelId);

            if ("Room deleted successfully".equals(response)) {
                logger.info("Room deleted successfully with ID: {}", hotelId);
                ApiResponse<String> apiResponse = new ApiResponse<>(200, "Success", response);
                return ResponseEntity.ok(apiResponse);
            } else {
                logger.error("Failed to delete room with ID {}: {}", hotelId, response);
                ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", response);
                return ResponseEntity.status(500).body(apiResponse);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while deleting room with ID {}: {}", hotelId, e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    //post report and issue by the vendor
    @PostMapping("/report")
    public ResponseEntity<?> postReport(
            @RequestBody ReportDto reportDto
            ){
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

    //get hotel details
    @GetMapping("/hotelDetails")
    public ResponseEntity<?> getHotelDetails(){
        try {
            Hotel hotel = vendorServiceImplementation.getHotelDetails();
            ApiResponse<Hotel> apiResponse = new ApiResponse<>(200, "Success", hotel);
            return ResponseEntity.ok(apiResponse);

        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<>(500, "Failed", e.getMessage());
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

}
