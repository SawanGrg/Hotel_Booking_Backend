package com.fyp.hotel.dto.admin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class AdminAnalyticsDto {

    private long totalUsers;
    private long totalVendors;
    private long totalHotels;
    private long totalRooms;
    private long totalBookings;

    public AdminAnalyticsDto() {
    }

    public AdminAnalyticsDto(long totalUsers, long totalVendors, long totalHotels,long totalRooms, long totalBookings) {
        this.totalUsers = totalUsers;
        this.totalVendors = totalVendors;
        this.totalHotels = totalHotels;
        this.totalRooms = totalRooms;
        this.totalBookings = totalBookings;
    }

}
