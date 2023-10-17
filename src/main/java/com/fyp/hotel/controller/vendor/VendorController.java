package com.fyp.hotel.controller.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.vendorDto.RoomDto;
import com.fyp.hotel.serviceImpl.vendor.VendorServiceImplementation;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) // maxage used to cache where the browser will not send the preflight request
@RequestMapping("/vendor")
public class VendorController {

    @Autowired
    private final ObjectMapper objectMapper;
    @Autowired
    private final VendorServiceImplementation vendorServiceImplementation;

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
    @PostMapping("/addhotelroom")
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

            RoomDto roomDto = this.objectMapper.readValue(stringRoomData, RoomDto.class);
            System.out.println("Hotel DTO : " + roomDto);

            String roomStatus = this.vendorServiceImplementation.addRooms(roomDto, stringHotelImage, stringHotelImage1, stringHotelImage2, stringHotelImage3);

            return ResponseEntity.ok("Hotel rooms added successfully");
        } catch (Exception e) {
            System.out.println("Error in vendor controller : " + e.getMessage());
            return ResponseEntity.badRequest().body("Error in vendor controller : " + e.getMessage());
            
        }
    }
}
