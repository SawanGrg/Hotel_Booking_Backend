package com.fyp.hotel.service.admin.user.Impl;

import com.fyp.hotel.dao.admin.AdminDAO;
import com.fyp.hotel.dao.hotel.HotelDAO;
import com.fyp.hotel.dto.BookingDTO;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.HotelRepository;
import com.fyp.hotel.repository.*;
import com.fyp.hotel.service.admin.user.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminDAO adminDAO;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private HotelDAO hotelDAO;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUserFilter(String userName, boolean ascending, int page, int size) {
        return adminDAO.getAllUserFilter(userName, ascending, page, size);
    }

    @Override
    public List<User> getAllVendorFilter(String userName, boolean ascending, int page, int size) {
        return adminDAO.getAllVendorFilter(userName, ascending, page, size);
    }

    @Override
    public User getUserProfile(long userId) {
        return adminDAO.getUserProfile(userId);
    }

    @Override
    public String verifyVendor(long userId, String status) {
        User user = adminDAO.getUserProfile(userId);
        if (user != null) {
            user.setUserStatus(status);
            userRepository.save(user);
            return "Vendor Verified";
        }
        return "Vendor not found";
    }

    @Override
    public String unVerifyUser(long userId, String status) {
        User user = adminDAO.getUserProfile(userId);
        if (user == null) return "User not found";

        if ("ROLE_VENDOR".equals(user.getRoles().iterator().next().getRoleName())) {
            Hotel preHotel = hotelRepository.findByUser(user);
            if (preHotel != null) {
                preHotel.setIsDeleted(true);
                preHotel.setHotelStatus(status);
                hotelDAO.updateHotel(preHotel);
            }
        }

        user.setUserStatus(status);
        userRepository.save(user);

        return "ROLE_VENDOR".equals(user.getRoles().iterator().next().getRoleName())
                ? "Vendor Unverified" : "User Unverified";
    }

    @Override
    public List<BookingDTO> getSpecificUserHotelBooking(long userId, int page, int size) {
        return adminDAO.getUserBookings(userId, page, size);
    }
}

