package com.fyp.hotel.controller.admin;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) 
@RequestMapping("/v1/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(){
        return "admin dashboard";
    }
    
}
