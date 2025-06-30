// AdminAnalyticsController.java
package com.fyp.hotel.controller.admin.analytics;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.admin.AdminAnalyticsDto;
import com.fyp.hotel.dto.admin.AdminRevenueDTO;
import com.fyp.hotel.service.admin.AdminServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin")
public class AdminAnalyticsController {

    @Autowired
    private AdminServiceFacade adminServiceFacade;

    @GetMapping("/adminRevenue")
    public ResponseEntity<?> adminRevenue() {
        AdminRevenueDTO revenue = adminServiceFacade.analyticsService.getAdminRevenue();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", revenue));
    }

    @GetMapping("/analytics")
    public ResponseEntity<?> adminAnalysis() {
        AdminAnalyticsDto analytics = adminServiceFacade.analyticsService.getAnalysisData();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", analytics));
    }
}