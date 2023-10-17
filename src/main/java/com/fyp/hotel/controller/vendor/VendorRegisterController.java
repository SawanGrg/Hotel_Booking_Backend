package com.fyp.hotel.controller.vendor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.RegisterDto;
import com.fyp.hotel.dto.userDto.UserDto;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.serviceImpl.vendor.VendorServiceImplementation;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) // maxage used to cache where the browser will not send the preflight request
public class VendorRegisterController {

    private ObjectMapper objectMapper;
    private VendorServiceImplementation vendorServiceImplementation;

    public VendorRegisterController(
            ObjectMapper objectMapper,
            VendorServiceImplementation vendorServiceImplementation
    ) {
        this.objectMapper = objectMapper;
        this.vendorServiceImplementation = vendorServiceImplementation;

    }

    //  @PostMapping("/register")
    // public ResponseEntity<RegisterDto> userRegister(
    //         @RequestParam("userData") String stringUserData,
    //         @RequestParam("userImage") MultipartFile stringUserImage) {

    //     try {
    //         User user = this.objectMapper.readValue(stringUserData, User.class);

    //         // Move image upload and default role assignment to the service
    //         String registerStatus = this.vendorServiceImplementation.registerVendor(user, stringUserImage);

    //         RegisterDto registerDto = new RegisterDto();
    //         registerDto.setMessage(registerStatus);
    //         registerDto.setStatus(HttpStatus.OK);

    //         return new ResponseEntity<RegisterDto>(registerDto, HttpStatus.OK);

    //     } catch (Exception e) {

    //         System.out.println("Error in user register controller : " + e.getMessage());
    //         RegisterDto registerDto = new RegisterDto();
    //         registerDto.setMessage(e.getMessage());
    //         registerDto.setStatus(HttpStatus.BAD_REQUEST);
    //         return new ResponseEntity<RegisterDto>(registerDto, HttpStatus.BAD_REQUEST);
    //     }
    // }

    @PostMapping("/register")
    public ResponseEntity<RegisterDto> vendorRegister(
        @Valid
        @RequestParam("vendorData") String stringVendorData,
        @RequestParam("vendorImage") MultipartFile stringVendorImage,
        @RequestParam("hotelData") String stringHotelData,
        @RequestParam("hotelImage") MultipartFile stringHotelImage
        ) {

            System.out.println("vendor data : " + stringVendorData);
            System.out.println("vendor image : " + stringVendorImage.getOriginalFilename());
            System.out.println("hotel data : " + stringHotelData);
            System.out.println("hotel image : " + stringHotelImage.getOriginalFilename());

        try {
           
            System.out.println("Step 1");
            //object mapper conversion in to java object 
            UserDto user = this.objectMapper.readValue(stringVendorData, UserDto.class);
             System.out.println("Step 2");
             System.out.println("user data : " + user);
            HotelDto hotel = this.objectMapper.readValue(stringHotelData, HotelDto.class);

             System.out.println("Step 3");
            System.out.println("user data : " + user);
            System.out.println("hotel data : " + hotel);

            String registerStatus = this.vendorServiceImplementation.registerVendor(user, stringVendorImage, hotel, stringHotelImage);

            RegisterDto registerDto = new RegisterDto();
            registerDto.setMessage(registerStatus);
            registerDto.setStatus(HttpStatus.OK);

            return new ResponseEntity<RegisterDto>(registerDto, HttpStatus.OK);

        } catch (Exception e) {

            System.out.println("Error in vendor register controller : " + e.getMessage());
            RegisterDto registerDto = new RegisterDto();
            registerDto.setMessage(e.getMessage());
            registerDto.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<RegisterDto>(registerDto, HttpStatus.BAD_REQUEST);

        }
    
        }
}
