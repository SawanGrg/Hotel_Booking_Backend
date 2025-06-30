// AdminBookingController.java
package com.fyp.hotel.controller.admin.booking;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.booking.BookingDTO;
import com.fyp.hotel.dto.common.PaginationRequestDTO;
import com.fyp.hotel.dto.user.IssueReportDTO;
import com.fyp.hotel.service.admin.AdminServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class AdminBookingController {

    @Autowired
    private AdminServiceFacade adminServiceFacade;

    @GetMapping("/viewAllReport")
    public ResponseEntity<?> viewAllReport(
            @ModelAttribute PaginationRequestDTO paginationRequestDTO
    ) {
        List<IssueReportDTO> reports = adminServiceFacade.analyticsService.getAllReports(paginationRequestDTO.getPageNumber(), paginationRequestDTO.getPageSize());
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", reports));
    }
}