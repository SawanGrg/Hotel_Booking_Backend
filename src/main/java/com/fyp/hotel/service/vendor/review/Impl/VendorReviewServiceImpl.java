package com.fyp.hotel.service.vendor.review.Impl;

import com.fyp.hotel.dao.hotel.HotelDAO;
import com.fyp.hotel.dto.review.ReviewDTO;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.HotelRepository;
import com.fyp.hotel.repository.UserRepository;
import com.fyp.hotel.service.vendor.review.VendorReviewService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorReviewServiceImpl implements VendorReviewService {

    private UserRepository userRepo;
    private HotelRepository hotelRepo;
    private HotelDAO hotelDAO;

    @Override
    @Transactional
    public List<ReviewDTO> getHotelReviews() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                String username = authentication.getName();
                User vendor = userRepo.findByUserName(username);
                Hotel hotel = hotelRepo.findByUser(vendor);
                return hotelDAO.getHotelReviews(hotel.getHotelId());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
