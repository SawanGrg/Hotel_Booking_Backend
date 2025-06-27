package com.fyp.hotel.service.admin.analytics.Impl;

import com.fyp.hotel.dao.admin.AdminDAO;
import com.fyp.hotel.dto.admin.AdminAnalyticsDto;
import com.fyp.hotel.dto.admin.AdminRevenueDTO;
import com.fyp.hotel.dto.userDto.IssueReportDTO;
import com.fyp.hotel.service.admin.analytics.AdminAnalyticsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    @Autowired
    private AdminDAO adminDAO;

    @Override
    public AdminAnalyticsDto getAnalysisData() {
        return new AdminAnalyticsDto(
                adminDAO.getTotalUsers(),
                adminDAO.getTotalVendors(),
                adminDAO.getTotalBookings(),
                adminDAO.getTotalHotels(),
                adminDAO.getTotalRooms()
        );
    }

    @Override
    public AdminRevenueDTO getAdminRevenue() {
        return new AdminRevenueDTO(
                adminDAO.getTotalCashOnArrivalRevenue(),
                adminDAO.getTotalKhaltiRevenue()
        );
    }

    @Override
    @Transactional
    public List<IssueReportDTO> getAllReports(int page, int size){
        return adminDAO.getAllReports();
    }
}
