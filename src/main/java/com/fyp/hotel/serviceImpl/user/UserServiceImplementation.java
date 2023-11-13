package com.fyp.hotel.serviceImpl.user;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fyp.hotel.model.Role;
import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.RoleRepository;
import com.fyp.hotel.repository.UserRepository;
import com.fyp.hotel.service.user.UserService;
import com.fyp.hotel.util.FileUploaderHelper;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImplementation implements UserService, UserDetailsService {

    @Autowired
    @Lazy
    private UserRepository userRepo;
    @Autowired
    @Lazy
    private RoleRepository roleRepo;
    @Autowired
    @Lazy
    private FileUploaderHelper fileUploaderHelper;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    private HotelRepository hotelRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("from class userservice implementation step 4  --->" + username);
        User user = this.userRepo.findByUserName(username);

        System.out.println("from class userservice implementation step 5  --->" + user);

        // Extract roles of a user based on the user details
        Set<Role> roles = this.roleRepo.findRolesByUser(user); // Corrected method name
        System.out.println("from class userservice implementation step 6  --->" + roles);

        user.setRoles(roles);

        System.out.println("from class userservice implementation step 7  --->" + user);

        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }
        return user;
    }

    @Transactional
    public User getUserWithRoles(String username) {
        User user = userRepo.findByUserName(username);
        // Accessing roles should work within the transaction.
        user.getRoles().size(); // This fetches the roles from the database.

        return user;
    }

    @Transactional
    public String registerUser(User user, MultipartFile userImage) {
        try {
            System.out.println(" this is the user password before encoding " + user.getPassword());

            String encodedPassword = passwordEncoder.encode(user.getPassword());

            System.out.println(" this is the user password after encoding " + encodedPassword);
            user.setPassword(encodedPassword);

            user.setCreatedAt(Instant.now());

            boolean isImageUploaded = fileUploaderHelper.fileUploader(userImage);
            String imageUrl = isImageUploaded ? "/images/" + userImage.getOriginalFilename() : "/images/default.png";
            user.setUserProfilePicture(imageUrl);
            user.setUserStatus("ACTIVE");
            Role defaultRole = roleRepo.findByRoleName("ROLE_USER");
            if (defaultRole != null) {
                user.getRoles().add(defaultRole);
            }

            userRepo.save(user);
            return "User registered successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //extract all the hotels details with pagination
    @Transactional
    public List<Hotel> getAllHotels(int page, int size) {
        return this.hotelRepo.findAll();
    }

    //update user profile
    @Transactional
    public String updateDetails(
            String userName,
            String userFirstName,
            String userLastName,
            String userEmail,
            String userPhone,
            String userAddress,
            String dateOfBirth
    ) {
        try {
            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
            if (authenticatedUser != null) {
                String authenticatedUserName = authenticatedUser.getName();
                User user = userRepo.findByUserName(authenticatedUserName);

                user.setUserName(userName.isEmpty() ? user.getUsername() : userName);
                user.setUserFirstName(userFirstName.isEmpty() ? user.getUserFirstName() : userFirstName);
                user.setUserLastName(userLastName.isEmpty() ? user.getUserLastName() : userLastName);
                user.setUserEmail(userEmail.isEmpty() ? user.getUserEmail() : userEmail);
                user.setUserPhone(userPhone.isEmpty() ? user.getUserPhone() : userPhone);
                user.setUserAddress(userAddress.isEmpty() ? user.getUserAddress() : userAddress);
                user.setDateOfBirth(dateOfBirth.isEmpty() ? user.getDateOfBirth() : dateOfBirth);
                user.setUpdatedAt(Instant.now());

                userRepo.save(user);
                return "User details updated successfully";
            } else {
                return "User not authenticated"; // Handle unauthenticated user
            }
        } catch (DataAccessException e) {
            return "Database error: " + e.getMessage(); // Handle database errors
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }

    @Transactional
    public String clearOutJwtToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            //setAuthenticated(false) will clear the context
            authentication.setAuthenticated(false);
            //clear the context holder
            SecurityContextHolder.clearContext();
            return "User logged out successfully";
        }
        else {
            return "User not authenticated";
        }
    }
}
