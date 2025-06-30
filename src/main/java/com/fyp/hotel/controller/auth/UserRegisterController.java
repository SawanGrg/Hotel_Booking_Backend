package com.fyp.hotel.controller.auth;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.service.user.UserServiceFacade;
import com.fyp.hotel.util.OtpMailSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.login.RegisterDto;
import com.fyp.hotel.model.User;

@RestController
@RequestMapping("/v1/user")
public class UserRegisterController {

    private ObjectMapper objectMapper;
    private UserServiceFacade userServiceFacade;
    private OtpMailSender otpMailSender;

    public UserRegisterController(
            ObjectMapper objectMapper,
            UserServiceFacade userServiceFacade,
            OtpMailSender otpMailSender
    ) {
        this.objectMapper = objectMapper;
        this.userServiceFacade = userServiceFacade;
        this.otpMailSender = otpMailSender;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDto> userRegister(
            @RequestParam("userData") String stringUserData,
            @RequestParam("userImage") MultipartFile stringUserImage)
            throws Exception
    {
        User user = this.objectMapper.readValue(stringUserData, User.class);
        if (this.userServiceFacade.userAuthenticationService.checkUserExist(user.getUsername())) {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setMessage("User already exist");
            registerDto.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(registerDto, HttpStatus.BAD_REQUEST);
        }
        String registerStatus = this.userServiceFacade.userAuthenticationService.registerUser(user, stringUserImage);
        RegisterDto registerDto = new RegisterDto();
        registerDto.setMessage(registerStatus);
        registerDto.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(registerDto, HttpStatus.OK);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam("OTP") String OTP
    ) {
        String status = this.userServiceFacade.userAuthenticationService.verifyOtp(OTP);
        if(status.equals("verified successfully")) {
            ApiResponse<String> apiResponse = new ApiResponse(200, "successfully registered user with OTP", status);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            ApiResponse<String> apiResponse = new ApiResponse(400, "Error in verify OTP", status);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}