package com.fyp.hotel.controller.admin;

import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.BookingDTO;
import com.fyp.hotel.dto.UserMessageDTO;
import com.fyp.hotel.dto.admin.AdminAnalyticsDto;
import com.fyp.hotel.dto.admin.AdminRevenueDTO;
import com.fyp.hotel.dto.userDto.BlogDTO;
import com.fyp.hotel.dto.userDto.IssueReportDTO;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.User;
import com.fyp.hotel.serviceImpl.admin.AdminServiceImlementation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) 
@RequestMapping("/v1/admin")
public class AdminController {

    @Autowired
    private AdminServiceImlementation adminServiceImlementation;

    @GetMapping("/viewAllUsers")
    public ResponseEntity<?> viewAllUsers(
            @RequestParam(value ="ascending", defaultValue = "true") boolean ascending,
            @RequestParam(value = "userName", defaultValue = "", required = false) String username,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ){
        try{
            List<User> user = this.adminServiceImlementation.getAllUserFilter(username, ascending, page, size);
            ApiResponse<List<User>> apiResponse = new ApiResponse<List<User>>(200, "Success", user);
            return ResponseEntity.ok(apiResponse);

        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<String>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
    }

    @GetMapping("/adminRevenue")
    public ResponseEntity<?> adminRevenue(){
        try{
            AdminRevenueDTO adminRevenueDTO = this.adminServiceImlementation.getAdminRevenue();
            ApiResponse<AdminRevenueDTO> apiResponse = new ApiResponse<AdminRevenueDTO>(200, "Success", adminRevenueDTO);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<String>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);

        }
    }



    @GetMapping("/viewAllVendors")
    public ResponseEntity<?> viewAllVendors(
            @RequestParam(value ="ascending", defaultValue = "true") boolean ascending,
            @RequestParam(value = "userName", defaultValue = "", required = false) String username,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){

        System.out.println("username: " + username + " ascending: " + ascending + " page: " + page + " size: " + size);
        try{
            List<User> user = this.adminServiceImlementation.getAllVendorFilter(username, ascending, page, size);
            ApiResponse<List<User>> apiResponse = new ApiResponse<List<User>>(200, "Success", user);
            return ResponseEntity.ok(apiResponse);

        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<String>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
    }

    //view all the hotels
    @GetMapping("/viewAllHotels")
    public ResponseEntity<?> viewAllHotels(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        try{
            List<HotelDto> hotels = this.adminServiceImlementation.getAllHotels(page, size);
            ApiResponse<List<HotelDto>> apiResponse = new ApiResponse<>(200, "Success", hotels);
            return ResponseEntity.ok(apiResponse);

        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<String>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
    }

    //for admin graph such as hotel number of rooms, number of users, number of vendors, number of bookings
    @GetMapping("/analytics")
    public ResponseEntity<?> adminAnalysis()
    {
        try{
            AdminAnalyticsDto adminAnalyticsDto = this.adminServiceImlementation.getAnalysisData();
            ApiResponse<AdminAnalyticsDto> apiResponse = new ApiResponse<AdminAnalyticsDto>(200, "Success", adminAnalyticsDto);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<String>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
    }

    //extract all the report and issue of the vendor
    @GetMapping("/viewAllReport")
    public  ResponseEntity<?> viewAllReport(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        try{
            List<IssueReportDTO> issueReportDTO = this.adminServiceImlementation.getAllReports(page, size);
            ApiResponse<List<IssueReportDTO>> apiResponse = new ApiResponse<List<IssueReportDTO>>(200, "Success", issueReportDTO);
            return ResponseEntity.ok(apiResponse);

        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<String>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
    }

    //extract all the blog of the user
    @GetMapping("/BlogBeforeVerification")
    public ResponseEntity<?> viewAllBlogBeforeVerification(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        try{
            List<BlogDTO> blogDTO = this.adminServiceImlementation.getAllBlogsBeforeVerification(page, size);
            ApiResponse<List<BlogDTO>> apiResponse = new ApiResponse<List<BlogDTO>>(200, "Success", blogDTO);
            return ResponseEntity.ok(apiResponse);

        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<String>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
    }


    //get specific blog
    @GetMapping("/specificBlog/{blogId}")
    public ResponseEntity<?> getSpecificBlog(
            @PathVariable("blogId") long blogId
    ){
        try{
            BlogDTO blogDTO = this.adminServiceImlementation.getSpecificBlog(blogId);
            ApiResponse<BlogDTO> apiResponse = new ApiResponse<>(200, "Success", blogDTO);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
    }

    //verify the blog of the user
    @PostMapping("/verifyBlog/{blogId}")
    public ResponseEntity<?> verifyBlog(
            @PathVariable("blogId") long blogId,
            @RequestParam(value = "status", defaultValue = "VERIFIED") String status
    ){
        try {
            String response = this.adminServiceImlementation.verifyBlog(blogId, status);
            ApiResponse<String> apiResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
    }

    //get specifc user hotel room booking based onthe user id
    @GetMapping("/getSpecificUserHotelBooking/{userId}")
    public ResponseEntity<?> getSpecificUserHotelBooking(
            @PathVariable("userId") long userId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        try{
            List<BookingDTO> hotelDtos = this.adminServiceImlementation.getSpecificUserHotelBooking(userId, page, size);
            ApiResponse<List<BookingDTO>> apiResponse = new ApiResponse<>(200, "Success", hotelDtos);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
    }

    //get user profile details based on the user id
    @GetMapping("/getUserProfile/{userId}")
    public ResponseEntity<?> getUserProfile(
            @PathVariable("userId") long userId
    ){
        try{
            User user = this.adminServiceImlementation.getUserProfile(userId);
            ApiResponse<User> apiResponse = new ApiResponse<>(200, "Success", user);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
    }

    //admin unverified the user and the vendor
    @PostMapping("/unverifyUser/{userId}")
    public ResponseEntity<?> unverifyUser(
            @PathVariable("userId") long userId,
            @RequestParam(value = "status", defaultValue = "UNVERIFIED") String status
    ){
        try{
            status = "UNVERIFIED";
            String response = this.adminServiceImlementation.unverifyUser(userId, status);
            ApiResponse<String> apiResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            // Handle the case when the user or hotel is not found
            ApiResponse<String> apiResponse = new ApiResponse<>(404, e.getMessage(), "Entity not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        } catch (Exception e){
            // Handle other unexpected exceptions
            ApiResponse<String> apiResponse = new ApiResponse<>(500, e.getMessage(), "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    //get specific hotel details based on the user id
     @GetMapping("/getSpecificHotel/{userId}")
    public ResponseEntity<?> getSpecificHotel(
            @PathVariable("userId") long userId
    ){
        try{
            HotelDto hotelDto = this.adminServiceImlementation.getHotelProfile(userId);
            ApiResponse<HotelDto> apiResponse = new ApiResponse<>(200, "Success", hotelDto);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
     }

    //admin verified the vendor
    @PostMapping("/verifyVendor/{userId}")
    public ResponseEntity<?> unverifyVendor(
            @PathVariable("userId") long userId,
            @RequestParam(value = "status", defaultValue = "VERIFIED") String status
    ){
        try{
            String response = this.adminServiceImlementation.verifyVendor(userId, status);
            ApiResponse<String> apiResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
    }

    //admin view all user message
    @GetMapping("/viewUserMessage")
    public ResponseEntity<?> getUserMessage(){
        try{
            List<UserMessageDTO> userMessageDTO = this.adminServiceImlementation.getAllUserMessages();
            ApiResponse<List<UserMessageDTO>> apiResponse = new ApiResponse<>(200, "Success", userMessageDTO);
            return ResponseEntity.ok(apiResponse);

        }catch (Exception e){
            ApiResponse<String> apiResponse = new ApiResponse<>(400, e.getMessage(), "Error");
            return ResponseEntity.ok(apiResponse);
        }
    }

}
