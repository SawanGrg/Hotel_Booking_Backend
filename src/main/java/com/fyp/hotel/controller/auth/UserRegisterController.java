package com.fyp.hotel.controller.auth;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.service.user.UserService;
import com.fyp.hotel.util.OtpMailSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.login.RegisterDto;
import com.fyp.hotel.model.User;
import com.fyp.hotel.service.user.userImpl.UserServiceImpl;

@RestController
@RequestMapping("/v1/user")
public class UserRegisterController {

    private ObjectMapper objectMapper;
    private UserService userServiceImpl;
    private OtpMailSender otpMailSender;

    public UserRegisterController(
            ObjectMapper objectMapper,
            UserServiceImpl userServiceImpl,
            OtpMailSender otpMailSender
    ) {
        this.objectMapper = objectMapper;
        this.userServiceImpl = userServiceImpl;
        this.otpMailSender = otpMailSender;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDto> userRegister(
            @RequestParam("userData") String stringUserData,
            @RequestParam("userImage") MultipartFile stringUserImage)
            throws Exception
    {
        User user = this.objectMapper.readValue(stringUserData, User.class);
        if (this.userServiceImpl.checkUserExist(user.getUsername())) {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setMessage("User already exist");
            registerDto.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(registerDto, HttpStatus.BAD_REQUEST);
        }
        String registerStatus = this.userServiceImpl.registerUser(user, stringUserImage);
        RegisterDto registerDto = new RegisterDto();
        registerDto.setMessage(registerStatus);
        registerDto.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(registerDto, HttpStatus.OK);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam("OTP") String OTP
    ) {
        String status = this.userServiceImpl.verifyOtp(OTP);
        if(status.equals("verified successfully")) {
            ApiResponse<String> apiResponse = new ApiResponse(200, "successfully registered user with OTP", status);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            ApiResponse<String> apiResponse = new ApiResponse(400, "Error in verify OTP", status);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}