package com.fyp.hotel.service.user.profile;

import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {
    String updateDetails(String userName,
                         String userFirstName,
                         String userLastName,
                         String userEmail,
                         String userPhone,
                         String userAddress,
                         String dateOfBirth,
                         MultipartFile userProfilePicture
    );
}