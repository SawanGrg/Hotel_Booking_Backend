package com.fyp.hotel.service.user.profile.Impl;

import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.UserRepository;
import com.fyp.hotel.util.FileUploaderHelper;
import com.fyp.hotel.service.user.profile.UserProfileService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepo;
    private final FileUploaderHelper fileUploaderHelper;

    @Autowired
    public UserProfileServiceImpl(UserRepository userRepo, FileUploaderHelper fileUploaderHelper) {
        this.userRepo = userRepo;
        this.fileUploaderHelper = fileUploaderHelper;
    }

    @Override
    public String updateDetails(
            String userName,
            String userFirstName,
            String userLastName,
            String userEmail,
            String userPhone,
            String userAddress,
            String dateOfBirth,
            MultipartFile userProfilePicture
    ) {
        try {
            boolean isProfilePictureProvided = false;
            if (userProfilePicture != null && !userProfilePicture.isEmpty()) {
                isProfilePictureProvided = fileUploaderHelper.fileUploader(userProfilePicture);
            }
            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
            if (authenticatedUser != null) {
                String authenticatedUserName = authenticatedUser.getName();
                User user = userRepo.findByUserName(authenticatedUserName);

                if (userName != null && !userName.isEmpty()) {
                    user.setUserName(userName);
                }
                if (userFirstName != null && !userFirstName.isEmpty()) {
                    user.setUserFirstName(userFirstName);
                }
                if (userLastName != null && !userLastName.isEmpty()) {
                    user.setUserLastName(userLastName);
                }
                if (userEmail != null && !userEmail.isEmpty()) {
                    user.setUserEmail(userEmail);
                }
                if (userPhone != null && !userPhone.isEmpty()) {
                    user.setUserPhone(userPhone);
                }
                if (userAddress != null && !userAddress.isEmpty()) {
                    user.setUserAddress(userAddress);
                }
                if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                    user.setDateOfBirth(dateOfBirth);
                }
                if (isProfilePictureProvided) {
                    user.setUserProfilePicture("/images/" + userProfilePicture.getOriginalFilename());
                }
                user.setUpdatedAt(Instant.now());
                userRepo.save(user);
                return "User details updated successfully";
            } else {
                return "User not authenticated";
            }
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }
}