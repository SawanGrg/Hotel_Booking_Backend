package com.fyp.hotel.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.ApiResponse;
import com.fyp.hotel.dto.userDto.BlogDTO;
import com.fyp.hotel.dto.userDto.UserProfileDto;
import com.fyp.hotel.service.user.userImpl.UserServiceImpl;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/user")
@Validated
public class BlogController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserProfileDto userProfileDto;

    @Autowired
    private ValueMapper valueMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/postBlog")
    public ResponseEntity<?> postBlog(
            @RequestParam("blogImage") MultipartFile blogImage,
            @RequestParam("blogDTO") String blogDTOJson
    ) throws JsonProcessingException {
        BlogDTO blogDTO = objectMapper.readValue(blogDTOJson, BlogDTO.class);
        String response = userServiceImpl.postUserBlog(blogImage, blogDTO);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }
}
