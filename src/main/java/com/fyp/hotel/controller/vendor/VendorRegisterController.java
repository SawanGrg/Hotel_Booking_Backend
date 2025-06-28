package com.fyp.hotel.controller.vendor;

import com.fyp.hotel.dto.vendorDto.VendorDto;
import com.fyp.hotel.service.user.userImpl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.login.RegisterDto;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.service.vendor.vendorImpl.VendorServiceImplementation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/vendor")
public class VendorRegisterController {

    private ObjectMapper objectMapper;
    private VendorServiceImplementation vendorServiceImplementation;
    private UserServiceImpl userServiceImpl;

    public VendorRegisterController(
            ObjectMapper objectMapper,
            VendorServiceImplementation vendorServiceImplementation,
            UserServiceImpl userServiceImpl
    ) {
        this.objectMapper = objectMapper;
        this.vendorServiceImplementation = vendorServiceImplementation;
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDto> vendorRegister(
            @Valid
            @RequestParam("vendorData") String stringVendorData,
            @RequestParam("vendorImage") MultipartFile stringVendorImage,
            @RequestParam("hotelData") String stringHotelData,
            @RequestParam("hotelImage") MultipartFile stringHotelImage
    ) throws Exception
    {
        VendorDto vendorDto = this.objectMapper.readValue(stringVendorData, VendorDto.class);
        HotelDto hotel = this.objectMapper.readValue(stringHotelData, HotelDto.class);

        if (this.userServiceImpl.checkUserExist(vendorDto.getUserName())) {
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

        return new ResponseEntity<>(registerDto, HttpStatus.OK);
    }
}