package com.fyp.hotel.controller.vendor;

import com.fyp.hotel.dto.vendorDto.VendorDto;
import com.fyp.hotel.service.user.userImpl.UserServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.login.RegisterDto;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.service.vendor.vendorImpl.VendorServiceImplementation;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/vendor")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) // maxage used to cache where the browser will not send the preflight request
public class VendorRegisterController {

    private ObjectMapper objectMapper;
    private VendorServiceImplementation vendorServiceImplementation;
    private UserServiceImplementation userServiceImplementation;

    public VendorRegisterController(
            ObjectMapper objectMapper,
            VendorServiceImplementation vendorServiceImplementation,
            UserServiceImplementation userServiceImplementation
    ) {
        this.objectMapper = objectMapper;
        this.vendorServiceImplementation = vendorServiceImplementation;
        this.userServiceImplementation = userServiceImplementation;

    }
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
            //object mapper conversion in to java object 
            VendorDto vendorDto = this.objectMapper.readValue(stringVendorData, VendorDto.class);
            HotelDto hotel = this.objectMapper.readValue(stringHotelData, HotelDto.class);


            if (this.userServiceImplementation.checkUserExist(vendorDto.getUserName())) {

                RegisterDto registerDto = new RegisterDto();

                registerDto.setMessage("User already exist");
                registerDto.setStatus(HttpStatus.BAD_REQUEST);

                return new ResponseEntity<>(registerDto, HttpStatus.BAD_REQUEST);

            }

            //check if the hotel pan is already exist
            if (this.vendorServiceImplementation.checkHotelPanExist(hotel.getHotelPan())) {

                RegisterDto registerDto = new RegisterDto();

                registerDto.setMessage("Hotel PAN already exist");
                registerDto.setStatus(HttpStatus.BAD_REQUEST);

                return new ResponseEntity<>(registerDto, HttpStatus.BAD_REQUEST);

            }

            String registerStatus = this.vendorServiceImplementation.registerVendor(vendorDto, stringVendorImage, hotel, stringHotelImage);

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
