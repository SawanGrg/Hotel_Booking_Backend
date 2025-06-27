package com.fyp.hotel.controller.user;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.userDto.*;
import com.fyp.hotel.service.user.userImpl.UserServiceImpl;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/v1/user")
@Validated // Add @Validated annotation to enable method-level validation
public class BlogController {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private  UserProfileDto userProfileDto;
    @Autowired
    private ValueMapper valueMapper;
    @Autowired
    private ObjectMapper objectMapper;

    //post blog by user
    @PostMapping("/postBlog")
    public ResponseEntity<?> postBlog(
            @RequestParam("blogImage") MultipartFile blogImage,
            @RequestParam("blogDTO") String blogDTOJson // Receive BlogDTO as JSON string
    ) {
        try {
            // Convert JSON string to BlogDTO object using ObjectMapper
            BlogDTO blogDTO = objectMapper.readValue(blogDTOJson, BlogDTO.class);

            // Call the service method with the MultipartFile and BlogDTO object
            String response = userServiceImpl.postUserBlog(blogImage, blogDTO);

            // Prepare success response
            ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.status(200).body(successResponse);
        } catch (IOException e) {
            // Handle JSON parsing exception
            ApiResponse<String> errorResponse = new ApiResponse<>(400, "Bad Request", "Invalid JSON format for BlogDTO");
            return ResponseEntity.status(400).body(errorResponse);
        } catch (Exception e) {
            // Handle other exceptions
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }



    // Exception handler for handling MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        ApiResponse<Map<String, String>> errorResponse = new ApiResponse<>(400, "Validation Error", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
