package com.fyp.hotel.dto.vendorDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorDashboardAnalyticsDTO {


//for div data
    private long totalBookings;

    private long totalRevenue;

    private long arrivalsToday;

    private long arrivalsTomorrow;

//    for pie chart and bar graph

    private long totalAvailableRooms;

    private long totalBookedRooms;

    private long totalCancelledRooms;

    private long totalRefundedRooms;

    private long totalPendingRooms;


    public VendorDashboardAnalyticsDTO() {
    }

    public VendorDashboardAnalyticsDTO(long totalBookings, long totalRevenue, long arrivalsToday, long arrivalsTomorrow, long totalAvailableRooms, long totalBookedRooms, long totalCancelledRooms, long totalRefundedRooms, long totalPendingRooms) {
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
        this.arrivalsToday = arrivalsToday;
        this.arrivalsTomorrow = arrivalsTomorrow;
        this.totalAvailableRooms = totalAvailableRooms;
        this.totalBookedRooms = totalBookedRooms;
        this.totalCancelledRooms = totalCancelledRooms;
        this.totalRefundedRooms = totalRefundedRooms;
        this.totalPendingRooms = totalPendingRooms;
    }


}
