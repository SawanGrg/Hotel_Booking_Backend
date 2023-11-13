package com.fyp.hotel.controller.user;

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
import com.fyp.hotel.model.User;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/v1/user")
public class UserRegisterController {

    private ObjectMapper objectMapper;
    private UserServiceImplementation userServiceImplementation;

    public UserRegisterController(
            ObjectMapper objectMapper,
            UserServiceImplementation userServiceImplementation
    ) {
        this.objectMapper = objectMapper;
        this.userServiceImplementation = userServiceImplementation;

    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDto> userRegister(
            @RequestParam("userData") String stringUserData,
            @RequestParam("userImage") MultipartFile stringUserImage) {

        System.out.println(
                stringUserData + " " + stringUserImage.getOriginalFilename() + " " + stringUserImage.getSize());
        try {
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

}
