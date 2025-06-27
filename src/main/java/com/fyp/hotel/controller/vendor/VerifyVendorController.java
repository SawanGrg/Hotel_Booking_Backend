package com.fyp.hotel.controller.vendor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.service.vendor.vendorImpl.VendorServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/vendor")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) // maxage used to cache where the browser will not send the preflight request
public class VerifyVendorController {

    private VendorServiceImplementation vendorServiceImplementation;
    private ObjectMapper objectMapper;

    @Autowired
    public VerifyVendorController(VendorServiceImplementation vendorServiceImplementation, ObjectMapper objectMapper) {
        this.vendorServiceImplementation = vendorServiceImplementation;
        this.objectMapper = objectMapper;
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verityVendor(
            @RequestParam("vendorId") String vendorId
    ) {
        return ResponseEntity.ok("Vendor Verified");
    }



}
