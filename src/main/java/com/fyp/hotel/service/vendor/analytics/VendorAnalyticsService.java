package com.fyp.hotel.service.vendor.analytics;

import com.fyp.hotel.dto.vendorDto.VendorDashboardAnalyticsDTO;
import com.fyp.hotel.dto.vendorDto.VendorRevenueDTO;

public interface VendorAnalyticsService {
    VendorDashboardAnalyticsDTO vendorAnalytics();
    VendorRevenueDTO getVendorRevenue();
}