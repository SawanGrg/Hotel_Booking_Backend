package com.fyp.hotel.service.user;

import com.fyp.hotel.service.user.auth.UserAuthenticationService;
import com.fyp.hotel.service.user.blog.UserBlogService;
import com.fyp.hotel.service.user.booking.UserBookingService;
import com.fyp.hotel.service.user.hotel.UserHotelService;
import com.fyp.hotel.service.user.message.UserMessageService;
import com.fyp.hotel.service.user.profile.UserProfileService;
import com.fyp.hotel.service.user.review.UserReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceFacade {

    public UserAuthenticationService userAuthenticationService;
    public UserBlogService userBlogService;
    public UserBookingService userBookingService;
    public UserHotelService userHotelService;
    public UserMessageService userMessageService;
    public UserProfileService userProfileService;
    public UserReviewService userReviewService;

    @Autowired
    public UserServiceFacade(UserAuthenticationService userAuthenticationService,
                             UserBlogService userBlogService,
                             UserBookingService userBookingService,
                             UserHotelService userHotelService,
                             UserMessageService userMessageService,
                             UserProfileService userProfileService,
                             UserReviewService userReviewService
    ) {
        this.userAuthenticationService = userAuthenticationService;
        this.userBlogService = userBlogService;
        this.userBookingService = userBookingService;
        this.userHotelService = userHotelService;
        this.userMessageService = userMessageService;
        this.userProfileService = userProfileService;
        this.userReviewService = userReviewService;
    }
}
