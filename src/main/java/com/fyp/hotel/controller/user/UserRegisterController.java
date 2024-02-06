package com.fyp.hotel.controller.user;

import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.util.OtpMailSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.RegisterDto;
import com.fyp.hotel.model.User;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/v1/user")
public class UserRegisterController {

    private ObjectMapper objectMapper;
    private UserServiceImplementation userServiceImplementation;
    private OtpMailSender otpMailSender;

    public UserRegisterController(
            ObjectMapper objectMapper,
            UserServiceImplementation userServiceImplementation,
            OtpMailSender otpMailSender
    ) {
        this.objectMapper = objectMapper;
        this.userServiceImplementation = userServiceImplementation;
        this.otpMailSender = otpMailSender;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDto> userRegister(
            @RequestParam("userData") String stringUserData,
            @RequestParam("userImage") MultipartFile stringUserImage) {

        System.out.println(
                stringUserData + " " + stringUserImage.getOriginalFilename() + " " + stringUserImage.getSize());
        try {
            // Convert string to user object
            User user = this.objectMapper.readValue(stringUserData, User.class);
            System.out.println("this is user data" + user.getPassword());
            // Move image upload and default role assignment to the service
            String registerStatus = this.userServiceImplementation.registerUser(user, stringUserImage);

            RegisterDto registerDto = new RegisterDto();
            registerDto.setMessage(registerStatus);
            registerDto.setStatus(HttpStatus.OK);

            return new ResponseEntity<>(registerDto, HttpStatus.OK);

        } catch (Exception e) {

            System.out.println("Error in user register controller : " + e.getMessage());
            RegisterDto registerDto = new RegisterDto();
            registerDto.setMessage(e.getMessage());
            registerDto.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(registerDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam("OTP") String OTP
    ){
        System.out.println("OTP : " + OTP);
        try{
            String status = this.userServiceImplementation.verifyOtp(OTP);
            if(status.equals("verified successfully")) {
                System.out.println("OTP verified");
                ApiResponse<String> apiResponse = new ApiResponse(200, "successfully registered user with OTP", status);
                return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            }else{
                System.out.println("OTP not verified");
                ApiResponse<String> apiResponse = new ApiResponse(400, "Error in verify OTP", status);
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            System.out.println("Error in verify OTP : " + e.getMessage());
            ApiResponse<String> apiResponse = new ApiResponse(400, "Error in verify OTP", e.getMessage());
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
