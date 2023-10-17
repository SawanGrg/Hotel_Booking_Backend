package com.fyp.hotel.controller.user;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) 
@RequestMapping("/user")
public class UserController {

    @GetMapping("/profile")
    public String userProfile() {
        return "User Profile";
    }
    
    @GetMapping("/home")
    public String userHome() {
        return "User Home";
    }
}
