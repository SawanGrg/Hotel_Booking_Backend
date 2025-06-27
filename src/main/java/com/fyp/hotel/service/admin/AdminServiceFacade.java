package com.fyp.hotel.service.admin;

import com.fyp.hotel.service.admin.analytics.AdminAnalyticsService;
import com.fyp.hotel.service.admin.blog.AdminBlogService;
import com.fyp.hotel.service.admin.hotel.AdminHotelService;
import com.fyp.hotel.service.admin.user.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceFacade {

    @Autowired
    public AdminAnalyticsService analyticsService;
    @Autowired
    public AdminBlogService blogService;
    @Autowired
    public AdminHotelService hotelService;
    @Autowired
    public AdminUserService userService;

}

