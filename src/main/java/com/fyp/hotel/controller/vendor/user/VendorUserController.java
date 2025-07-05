package com.fyp.hotel.controller.vendor.user;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.hotel.HotelDto;
import com.fyp.hotel.dto.vendorDto.ReportDto;
import com.fyp.hotel.dto.vendorDto.VendorDto;
import com.fyp.hotel.service.user.UserServiceFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.service.vendor.VendorServiceFacade;
import com.fyp.hotel.util.ValueMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/vendor")
public class VendorUserController {

    private VendorServiceFacade vendorService;
    private UserServiceFacade userServiceFacade;
    private ObjectMapper objectMapper;
    private ValueMapper valueMapper;

    @Autowired
    public VendorUserController(VendorServiceFacade vendorService, UserServiceFacade userServiceFacade, ObjectMapper objectMapper, ValueMapper valueMapper) {
        this.vendorService = vendorService;
        this.userServiceFacade = userServiceFacade;
        this.objectMapper = objectMapper;
        this.valueMapper = valueMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> vendorRegister(
            @Valid @RequestParam("vendorData") String vendorDataJson,
            @RequestParam("vendorImage") MultipartFile vendorImage,
            @RequestParam("hotelData") String hotelDataJson,
            @RequestParam("hotelImage") MultipartFile hotelImage
    ) throws Exception {
        VendorDto vendorDto = objectMapper.readValue(vendorDataJson, VendorDto.class);
        if (this.userServiceFacade.userAuthenticationService.checkUserExist(vendorDto.getUserName())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "User exists", null));
        }
        String status = vendorService.getVendorUserService().registerVendor(vendorDto,
                vendorImage,
                objectMapper.readValue(hotelDataJson, HotelDto.class),
                hotelImage);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", status));
    }

    @PostMapping("/report")
    public ResponseEntity<?> postReport(@RequestBody ReportDto reportDto) {
        String status = vendorService.getVendorReportService().postReport(valueMapper.conversionToReport(reportDto));
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", status));
    }
}