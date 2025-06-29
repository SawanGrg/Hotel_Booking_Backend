package com.fyp.hotel.service.admin.analytics;

import com.fyp.hotel.dto.admin.AdminAnalyticsDto;
import com.fyp.hotel.dto.admin.AdminRevenueDTO;
import com.fyp.hotel.dto.user.IssueReportDTO;

import java.util.List;

public interface AdminAnalyticsService {
    AdminAnalyticsDto getAnalysisData();
    AdminRevenueDTO getAdminRevenue();
    List<IssueReportDTO> getAllReports(int page, int size);
}

