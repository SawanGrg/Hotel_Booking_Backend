package com.fyp.hotel.service.user.review.Impl;

import com.fyp.hotel.dto.hotel.HotelReviewDTO;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.Review;
import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.HotelRepository;
import com.fyp.hotel.repository.ReviewRepository;
import com.fyp.hotel.repository.UserRepository;
import com.fyp.hotel.service.user.review.UserReviewService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserReviewServiceImpl implements UserReviewService {

    private final ReviewRepository hotelReviewRepository;
    private final UserRepository userRepo;
    private final HotelRepository hotelRepo;

    @Autowired
    public UserReviewServiceImpl(ReviewRepository hotelReviewRepository,
                                 UserRepository userRepo,
                                 HotelRepository hotelRepo) {
        this.hotelReviewRepository = hotelReviewRepository;
        this.userRepo = userRepo;
        this.hotelRepo = hotelRepo;
    }

    @Override
    public String postHotelReviewByUser(long hotelId, HotelReviewDTO hotelReviewDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User user = userRepo.findByUserName(userName);
            Hotel hotel = hotelRepo.findById(hotelId)
                    .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + hotelId));

            Review review = new Review();
            review.setReviewContent(hotelReviewDTO.getHotelReview());
            review.setHotel(hotel);
            review.setUser(user);
            review.setCreatedDate(Instant.now().toString());

            hotelReviewRepository.save(review);
            return "Review posted successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to post review.";
        }
    }

    @Override
    public List<HotelReviewDTO> getAllHotelReviews(Long hotelId) {
        List<Review> reviews = hotelReviewRepository.findByHotelId(hotelId);
        return reviews.stream()
                .map(review -> new HotelReviewDTO(
                        review.getHotel().getHotelId(),
                        review.getReviewContent(),
                        review.getUser().getUsername(),
                        review.getCreatedDate()
                ))
                .collect(Collectors.toList());
    }
}