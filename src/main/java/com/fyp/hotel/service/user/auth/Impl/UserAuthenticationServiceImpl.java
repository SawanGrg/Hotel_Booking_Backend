package com.fyp.hotel.service.user.auth.Impl;

import com.fyp.hotel.dao.user.UserDAO;
import com.fyp.hotel.dto.user.UserChangePasswordDto;
import com.fyp.hotel.model.Role;
import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.RoleRepository;
import com.fyp.hotel.repository.UserRepository;
import com.fyp.hotel.service.user.auth.UserAuthenticationService;
import com.fyp.hotel.util.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class UserAuthenticationServiceImpl implements UserAuthenticationService, UserDetailsService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final FileUploaderHelper fileUploaderHelper;
    private final PasswordEncoder passwordEncoder;
    private final OtpMailSender otpMailSender;
    private final EmailSenderService emailSenderService;
    private final OtpGenerator otpGenerator;
    private final UserDAO userDAO;

    private Map<String, String> storeOtpAndUserName = new ConcurrentHashMap<>();

    @Autowired
    public UserAuthenticationServiceImpl(
            UserRepository userRepo,
            RoleRepository roleRepo,
            FileUploaderHelper fileUploaderHelper,
            PasswordEncoder passwordEncoder,
            OtpMailSender otpMailSender,
            EmailSenderService emailSenderService,
            OtpGenerator otpGenerator,
            UserDAO userDAO) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.fileUploaderHelper = fileUploaderHelper;
        this.passwordEncoder = passwordEncoder;
        this.otpMailSender = otpMailSender;
        this.emailSenderService = emailSenderService;
        this.otpGenerator = otpGenerator;
        this.userDAO = userDAO;
    }

    @Override
    public boolean checkUserExist(String userName) {
        if(userDAO.isUserExist(userName)){
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.findByUserName(username);
        Set<Role> roles = this.roleRepo.findRolesByUser(user);
        user.setRoles(roles);
        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }
        return user;
    }

    @Override
    public String registerUser(User user, MultipartFile userImage) {
        try {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setCreatedAt(Instant.now());
            boolean isImageUploaded = fileUploaderHelper.fileUploader(userImage);
            String imageUrl = isImageUploaded ? "/images/" + userImage.getOriginalFilename() : "/images/default.png";
            user.setUserProfilePicture(imageUrl);
            user.setUserStatus("PENDING");
            Role defaultRole = roleRepo.findByRoleName("ROLE_USER");
            if (defaultRole != null) {
                user.getRoles().add(defaultRole);
            }

            userRepo.save(user);

            //generate otp
            String otp = otpGenerator.generateOTP();
            //store in map
            storeOtpAndUserName.put(otp, user.getUsername());
            //pass otp and set mail message
            Map<String, String> sendMail = otpMailSender.getVerifyEmailMessage(otp);
            //send mail to the specific user
            emailSenderService.sendEmail(
                    user.getUserEmail(),
                    sendMail.get("subject"),
                    sendMail.get("message"));

            return "User registered successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public String verifyOtp(String otp) {
        try {
            String userName = storeOtpAndUserName.get(otp);
            User user = userRepo.findByUserName(userName);
            user.setUserStatus("VERIFIED");
            userRepo.save(user);
            return "verified successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public String changePassword(UserChangePasswordDto userChangePasswordDto) {
        try {
            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
            if (authenticatedUser != null) {
                String authenticatedUserName = authenticatedUser.getName();
                User user = userRepo.findByUserName(authenticatedUserName);

                String oldPassword = userChangePasswordDto.getOldPassword();
                String newPassword = userChangePasswordDto.getNewPassword();

                if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setUpdatedAt(Instant.now());
                    userRepo.save(user);
                    return "Password changed successfully";
                } else {
                    return "Old password is incorrect";
                }
            } else {
                return "User not authenticated";
            }
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }

    @Override
    public String clearOutJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            authentication.setAuthenticated(false);
            SecurityContextHolder.clearContext();
            return "User logged out successfully";
        }
        else {
            return "User not authenticated";
        }
    }

    @Override
    public User getUserById() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUserName(name);
    }
}