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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            @RequestParam(value ="ascending", defaultValue = "true") boolean ascending,
            @RequestParam(value = "userName", defaultValue = "", required = false) String username,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ){
        try{
            List<User> user = this.adminServiceFacade.userService.getAllUserFilter(username, ascending, page, size);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", user));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }

    @GetMapping("/adminRevenue")
    public ResponseEntity<?> adminRevenue(){
        try{
            AdminRevenueDTO adminRevenueDTO = this.adminServiceFacade.analyticsService.getAdminRevenue();
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", adminRevenueDTO));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }

    @GetMapping("/viewAllVendors")
    public ResponseEntity<?> viewAllVendors(
            @RequestParam(value ="ascending", defaultValue = "true") boolean ascending,
            @RequestParam(value = "userName", defaultValue = "", required = false) String username,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        try{
            List<User> user = this.adminServiceFacade.userService.getAllVendorFilter(username, ascending, page, size);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", user));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }

    @GetMapping("/viewAllHotels")
    public ResponseEntity<?> viewAllHotels(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        try{
            List<HotelDto> hotels = this.adminServiceFacade.hotelService.getAllHotels(page, size);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", hotels));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }

    @GetMapping("/analytics")
    public ResponseEntity<?> adminAnalysis() {
        try{
            AdminAnalyticsDto adminAnalyticsDto = this.adminServiceFacade.analyticsService.getAnalysisData();
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", adminAnalyticsDto));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }

    @GetMapping("/viewAllReport")
    public ResponseEntity<?> viewAllReport(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        try{
            List<IssueReportDTO> issueReportDTO = this.adminServiceFacade.analyticsService.getAllReports(page, size);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", issueReportDTO));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }

    @GetMapping("/BlogBeforeVerification")
    public ResponseEntity<?> viewAllBlogBeforeVerification(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        try{
            List<BlogDTO> blogDTO = this.adminServiceFacade.blogService.getAllBlogsBeforeVerification(page, size);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", blogDTO));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }

    @GetMapping("/specificBlog/{blogId}")
    public ResponseEntity<?> getSpecificBlog(@PathVariable("blogId") long blogId){
        try{
            BlogDTO blogDTO = this.adminServiceFacade.blogService.getSpecificBlog(blogId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", blogDTO));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }

    @PostMapping("/verifyBlog/{blogId}")
    public ResponseEntity<?> verifyBlog(
            @PathVariable("blogId") long blogId,
            @RequestParam(value = "status", defaultValue = "VERIFIED") String status
    ){
        try {
            String response = this.adminServiceFacade.blogService.verifyBlog(blogId, status);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }

    @GetMapping("/getSpecificUserHotelBooking/{userId}")
    public ResponseEntity<?> getSpecificUserHotelBooking(
            @PathVariable("userId") long userId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        try{
            List<BookingDTO> hotelDtos = this.adminServiceFacade.userService.getSpecificUserHotelBooking(userId, page, size);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", hotelDtos));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }

    @GetMapping("/getUserProfile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable("userId") long userId){
        try{
            User user = this.adminServiceFacade.userService.getUserProfile(userId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", user));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }

    @PostMapping("/unverifyUser/{userId}")
    public ResponseEntity<?> unVerifyUser(
            @PathVariable("userId") long userId,
            @RequestParam(value = "status", defaultValue = "UNVERIFIED") String status
    ){
        try{
            status = "UNVERIFIED";
            String response = this.adminServiceFacade.userService.unVerifyUser(userId, status);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, e.getMessage(), "Entity not found"));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(500, e.getMessage(), "Internal Server Error"));
        }
    }

    @GetMapping("/getSpecificHotel/{userId}")
    public ResponseEntity<?> getSpecificHotel(@PathVariable("userId") long userId){
        try{
            HotelDto hotelDto = this.adminServiceFacade.hotelService.getHotelProfile(userId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", hotelDto));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }

    @PostMapping("/verifyVendor/{userId}")
    public ResponseEntity<?> unverifyVendor(
            @PathVariable("userId") long userId,
            @RequestParam(value = "status", defaultValue = "VERIFIED") String status
    ){
        try{
            String response = this.adminServiceFacade.userService.verifyVendor(userId, status);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse<>(400, e.getMessage(), "Error"));
        }
    }
}

