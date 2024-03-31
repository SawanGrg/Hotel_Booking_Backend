package com.fyp.hotel.controller.admin;

import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.admin.AdminAnalyticsDto;
import com.fyp.hotel.dto.userDto.BlogDTO;
import com.fyp.hotel.dto.userDto.IssueReportDTO;
import com.fyp.hotel.model.User;
import com.fyp.hotel.serviceImpl.admin.AdminServiceImlementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600) 
@RequestMapping("/v1/admin")
public class AdminController {

    @Autowired
    private AdminServiceImlementation adminServiceImlementation;

    @GetMapping("/dashboard")
    public String dashboard(){
        return "admin dashboard";
    }

    @GetMapping("/viewAllUsers")
    public ResponseEntity<?> viewAllUsers(
            @RequestParam(value ="ascending", defaultValue = "true") boolean ascending,
            @RequestParam(value = "userName", defaultValue = "", required = false) String username,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){

        System.out.println("username: " + username + " ascending: " + ascending + " page: " + page + " size: " + size);
        try{
            List<User> user = this.adminServiceImlementation.getAllUserFilter(username, ascending, page, size);
            ApiResponse<List<User>> apiResponse = new ApiResponse<List<User>>(200, "Success", user);
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

}
