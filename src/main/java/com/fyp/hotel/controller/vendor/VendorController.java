package com.fyp.hotel.controller.vendor;

import com.fyp.hotel.model.HotelRoom;
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
    public ResponseEntity<String> addHotelRooms(
        @RequestParam("roomData") String stringRoomData,
        @RequestParam("mainRoomImage") MultipartFile stringHotelImage,
        @RequestParam("roomImage1") MultipartFile stringHotelImage1,
        @RequestParam("roomImage2") MultipartFile stringHotelImage2,
        @RequestParam("roomImage3") MultipartFile stringHotelImage3
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

            return ResponseEntity.ok("Hotel rooms added successfully");
        } catch (Exception e) {
            System.out.println("Error in vendor controller : " + e.getMessage());
            return ResponseEntity.badRequest().body("Error in vendor controller : " + e.getMessage());
            
        }
    }

    @GetMapping("/hotelRooms")
    public ResponseEntity<Page<HotelRoom>> getHotelRooms(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        try {
            Page<HotelRoom> hotelRooms = vendorServiceImplementation.getHotelRooms(page, size);
            return ResponseEntity.ok(hotelRooms);
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
                return ResponseEntity.ok(response);
            } else {
                logger.error("Failed to delete room with ID {}: {}", hotelId, response);
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while deleting room with ID {}: {}", hotelId, e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }
}
