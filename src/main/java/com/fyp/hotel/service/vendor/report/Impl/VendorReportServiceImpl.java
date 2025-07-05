package com.fyp.hotel.service.vendor.report.Impl;

import com.fyp.hotel.model.Report;
import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.ReportRepository;
import com.fyp.hotel.repository.UserRepository;
import com.fyp.hotel.service.vendor.report.VendorReportService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class VendorReportServiceImpl implements VendorReportService {

    private UserRepository userRepo;
    private ReportRepository reportRepository;

    @Override
    @Transactional
    public String postReport(Report report) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                String username = authentication.getName();

                User vendorObject = userRepo.findByUserName(username);

                report.setUser(vendorObject);

                reportRepository.save(report);

                return "Report posted successfully";
            }
            return "Authentication failed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to post report";
        }
    }
}
