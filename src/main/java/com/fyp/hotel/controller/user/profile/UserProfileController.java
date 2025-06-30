package com.fyp.hotel.controller.user.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.user.*;
import com.fyp.hotel.model.User;
import com.fyp.hotel.service.user.UserServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/user")
public class UserProfileController {

    private final ObjectMapper objectMapper;
    private UserServiceFacade userServiceFacade;

    @Autowired
    public UserProfileController(UserServiceFacade userServiceFacade, ObjectMapper objectMapper) {
        this.userServiceFacade = userServiceFacade;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/view-user-details")
    public ResponseEntity<?> viewUserDetails() {
        User user = this.userServiceFacade.userAuthenticationService.getUserById();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", user));
    }

    @PostMapping("/update-user-details")
    public ResponseEntity<?> updateUserDetails(
            @RequestParam(value = "userUpgradingDetails", required = false) String userUpgradingDetailsJson,
            @RequestParam(value = "userProfileImage", required = false) MultipartFile userProfileImage
    ) throws Exception
    {
        UserProfileDto userUpgradingDetails = null;
        if (userUpgradingDetailsJson != null) {
            userUpgradingDetails = objectMapper.readValue(userUpgradingDetailsJson, UserProfileDto.class);
        }
        String userProfileImageFilename = userProfileImage != null ? userProfileImage.getOriginalFilename() : null;
        String response = this.userServiceFacade.userProfileService.updateDetails(
                userUpgradingDetails != null ? userUpgradingDetails.getUserName() : null,
                userUpgradingDetails != null ? userUpgradingDetails.getUserFirstName() : null,
                userUpgradingDetails != null ? userUpgradingDetails.getUserLastName() : null,
                userUpgradingDetails != null ? userUpgradingDetails.getUserEmail() : null,
                userUpgradingDetails != null ? userUpgradingDetails.getUserPhone() : null,
                userUpgradingDetails != null ? userUpgradingDetails.getUserAddress() : null,
                userUpgradingDetails != null ? userUpgradingDetails.getDateOfBirth() : null,
                userProfileImage
        );
        if ("User details updated successfully".equals(response)) {
            ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.status(200).body(successResponse);
        } else {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "An error occurred", response);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/user-change-password")
    public ResponseEntity<?> changePassword(@RequestBody UserChangePasswordDto dto) {
        String response = this.userServiceFacade.userAuthenticationService.changePassword(dto);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        String response = this.userServiceFacade.userAuthenticationService.clearOutJwtToken();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }

    @PostMapping("/getInTouch")
    public ResponseEntity<?> getInTouch(
            @RequestBody UserMessageDTO userMessageDTO
    ) {
        String response = this.userServiceFacade.userMessageService.postUserMessage(userMessageDTO);
        ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
        return ResponseEntity.status(200).body(successResponse);
    }
}