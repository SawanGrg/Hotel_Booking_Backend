package com.fyp.hotel.service.user.auth;

import com.fyp.hotel.dto.user.UserChangePasswordDto;
import com.fyp.hotel.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface UserAuthenticationService extends UserDetailsService {
    boolean checkUserExist(String userName);
    String registerUser(User user, MultipartFile userImage);
    String verifyOtp(String otp);
    String changePassword(UserChangePasswordDto userChangePasswordDto);
    String clearOutJwtToken();
    User getUserById();
}