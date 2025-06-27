package com.fyp.hotel.service.admin.user;

import com.fyp.hotel.dto.BookingDTO;
import com.fyp.hotel.model.User;

import java.util.List;

public interface AdminUserService {
    List<User> getAllUserFilter(String userName, boolean ascending, int page, int size);
    List<User> getAllVendorFilter(String userName, boolean ascending, int page, int size);
    User getUserProfile(long userId);
    String unVerifyUser(long userId, String status);
    String verifyVendor(long userId, String status);
    List<BookingDTO> getSpecificUserHotelBooking(long userId, int page, int size);
}
