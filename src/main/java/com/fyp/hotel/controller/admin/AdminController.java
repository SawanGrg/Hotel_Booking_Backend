package com.fyp.hotel.controller.admin;

import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.BookingDTO;
import com.fyp.hotel.dto.admin.AdminAnalyticsDto;
import com.fyp.hotel.dto.admin.AdminRevenueDTO;
import com.fyp.hotel.dto.userDto.BlogDTO;
import com.fyp.hotel.dto.userDto.IssueReportDTO;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.model.User;
import com.fyp.hotel.service.admin.AdminServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    @Autowired
    private AdminServiceFacade adminServiceFacade;

    @GetMapping("/viewAllUsers")
    public ResponseEntity<?> viewAllUsers(
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(defaultValue = "", required = false) String username,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<User> users = adminServiceFacade.userService.getAllUserFilter(username, ascending, page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", users));
    }

    @GetMapping("/adminRevenue")
    public ResponseEntity<?> adminRevenue() {
        AdminRevenueDTO revenue = adminServiceFacade.analyticsService.getAdminRevenue();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", revenue));
    }

    @GetMapping("/viewAllVendors")
    public ResponseEntity<?> viewAllVendors(
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(defaultValue = "", required = false) String username,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<User> vendors = adminServiceFacade.userService.getAllVendorFilter(username, ascending, page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", vendors));
    }

    @GetMapping("/viewAllHotels")
    public ResponseEntity<?> viewAllHotels(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<HotelDto> hotels = adminServiceFacade.hotelService.getAllHotels(page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", hotels));
    }

    @GetMapping("/analytics")
    public ResponseEntity<?> adminAnalysis() {
        AdminAnalyticsDto analytics = adminServiceFacade.analyticsService.getAnalysisData();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", analytics));
    }

    @GetMapping("/viewAllReport")
    public ResponseEntity<?> viewAllReport(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<IssueReportDTO> reports = adminServiceFacade.analyticsService.getAllReports(page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", reports));
    }

    @GetMapping("/BlogBeforeVerification")
    public ResponseEntity<?> viewAllBlogBeforeVerification(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<BlogDTO> blogs = adminServiceFacade.blogService.getAllBlogsBeforeVerification(page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", blogs));
    }

    @GetMapping("/specificBlog/{blogId}")
    public ResponseEntity<?> getSpecificBlog(@PathVariable long blogId) {
        BlogDTO blog = adminServiceFacade.blogService.getSpecificBlog(blogId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", blog));
    }

    @PostMapping("/verifyBlog/{blogId}")
    public ResponseEntity<?> verifyBlog(
            @PathVariable long blogId,
            @RequestParam(defaultValue = "VERIFIED") String status
    ) {
        String response = adminServiceFacade.blogService.verifyBlog(blogId, status);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }

    @GetMapping("/getSpecificUserHotelBooking/{userId}")
    public ResponseEntity<?> getSpecificUserHotelBooking(
            @PathVariable long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<BookingDTO> bookings = adminServiceFacade.userService.getSpecificUserHotelBooking(userId, page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", bookings));
    }

    @GetMapping("/getUserProfile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable long userId) {
        User user = adminServiceFacade.userService.getUserProfile(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", user));
    }

    @PostMapping("/unverifyUser/{userId}")
    public ResponseEntity<?> unVerifyUser(
            @PathVariable long userId,
            @RequestParam(defaultValue = "UNVERIFIED") String status
    ) {
        String response = adminServiceFacade.userService.unVerifyUser(userId, status);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }

    @GetMapping("/getSpecificHotel/{userId}")
    public ResponseEntity<?> getSpecificHotel(@PathVariable long userId) {
        HotelDto hotel = adminServiceFacade.hotelService.getHotelProfile(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", hotel));
    }

    @PostMapping("/verifyVendor/{userId}")
    public ResponseEntity<?> verifyVendor(
            @PathVariable long userId,
            @RequestParam(defaultValue = "VERIFIED") String status
    ) {
        String response = adminServiceFacade.userService.verifyVendor(userId, status);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }
}
