package com.fyp.hotel.service.user;

import com.fyp.hotel.model.Hotel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    public List<Hotel> getAllHotels(int page, int size);
    public String updateDetails(
            String userName,
            String userFirstName,
            String userLastName,
            String userEmail,
            String userPhone,
            String userAddress,
            String dateOfBirth,
            MultipartFile userProfilePicture
    );

}
