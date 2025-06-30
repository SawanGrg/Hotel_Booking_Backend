package com.fyp.hotel.service.admin;

import com.fyp.hotel.service.admin.analytics.AdminAnalyticsService;
import com.fyp.hotel.service.admin.blog.AdminBlogService;
import com.fyp.hotel.service.admin.hotel.AdminHotelService;
import com.fyp.hotel.service.admin.user.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceFacade {

    public AdminAnalyticsService analyticsService;
    public AdminBlogService blogService;
    public AdminHotelService hotelService;
    public AdminUserService userService;

    @Autowired
    public AdminServiceFacade(AdminAnalyticsService analyticsService,
                              AdminBlogService blogService,
                              AdminHotelService hotelService,
                              AdminUserService userService) {
        this.analyticsService = analyticsService;
        this.blogService = blogService;
        this.hotelService = hotelService;
        this.userService = userService;
    }
}

