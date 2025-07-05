package com.fyp.hotel.service.vendor;

import com.fyp.hotel.service.vendor.analytics.VendorAnalyticsService;
import com.fyp.hotel.service.vendor.booking.VendorBookingService;
import com.fyp.hotel.service.vendor.hotel.VendorHotelService;
import com.fyp.hotel.service.vendor.report.VendorReportService;
import com.fyp.hotel.service.vendor.review.VendorReviewService;
import com.fyp.hotel.service.vendor.room.VendorRoomService;
import com.fyp.hotel.service.vendor.user.VendorUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class VendorServiceFacade {

    private final VendorAnalyticsService vendorAnalyticsService;
    private final VendorBookingService vendorBookingService;
    private final VendorHotelService vendorHotelService;
    private final VendorReportService vendorReportService;
    private final VendorReviewService vendorReviewService;
    private final VendorRoomService vendorRoomService;
    private final VendorUserService vendorUserService;

}
