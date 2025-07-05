package com.fyp.hotel.controller.vendor.analytics;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.vendorDto.VendorDashboardAnalyticsDTO;
import com.fyp.hotel.dto.vendorDto.VendorRevenueDTO;
import com.fyp.hotel.service.vendor.VendorServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/vendor")
public class VendorAnalyticsController {

    @Autowired
    private VendorServiceFacade vendorServiceFacade;

    @GetMapping("/analytics")
    public ResponseEntity<?> getVendorAnalytics() {
        VendorDashboardAnalyticsDTO vendorDashboardAnalyticsDTO = vendorServiceFacade.getVendorAnalyticsService().vendorAnalytics();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", vendorDashboardAnalyticsDTO));
    }

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue() {
        VendorRevenueDTO revenueDTO = vendorServiceFacade.getVendorAnalyticsService().getVendorRevenue();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", revenueDTO));
    }
}