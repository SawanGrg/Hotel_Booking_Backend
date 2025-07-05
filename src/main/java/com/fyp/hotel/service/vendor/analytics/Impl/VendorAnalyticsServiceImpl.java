package com.fyp.hotel.service.vendor.analytics.Impl;

import com.fyp.hotel.dao.booking.BookingDAO;
import com.fyp.hotel.dao.hotel.HotelDAO;
import com.fyp.hotel.dto.vendorDto.VendorDashboardAnalyticsDTO;
import com.fyp.hotel.dto.vendorDto.VendorRevenueDTO;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.PaymentMethod;
import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.*;
import com.fyp.hotel.service.vendor.analytics.VendorAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VendorAnalyticsServiceImpl implements VendorAnalyticsService {

    private final HotelDAO hotelDAO;
    private final BookingDAO bookingDAO;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public VendorDashboardAnalyticsDTO vendorAnalytics() {
        Hotel hotel = getCurrentVendorHotel();
        VendorDashboardAnalyticsDTO analyticsDTO = new VendorDashboardAnalyticsDTO();

        analyticsDTO.setTotalBookings(hotelDAO.getTotalBookings(hotel.getHotelId()));
        analyticsDTO.setTotalRefundedRooms(hotelDAO.getTotalRefundedBookings(hotel.getHotelId()));
        analyticsDTO.setTotalCancelledRooms(hotelDAO.getTotalCancelledsBookings(hotel.getHotelId()));
        analyticsDTO.setTotalPendingRooms(hotelDAO.getTotalPendingBookings(hotel.getHotelId()));
        analyticsDTO.setTotalBookedRooms(hotelDAO.getTotalBookedBookings(hotel.getHotelId()));
        analyticsDTO.setTotalRevenue(hotelDAO.getHotelTotalRevenue(hotel.getHotelId()));
        analyticsDTO.setArrivalsToday(hotelDAO.getTodayTotalArrivingRooms(hotel.getHotelId(), LocalDate.now()));
        analyticsDTO.setArrivalsTomorrow(hotelDAO.getTomorrowTotalArrivingRooms(hotel.getHotelId()));
        analyticsDTO.setTotalAvailableRooms(hotelDAO.getTodayAvailableRoom(hotel.getHotelId()));

        return analyticsDTO;
    }

    @Override
    public VendorRevenueDTO getVendorRevenue() {
        Hotel hotel = getCurrentVendorHotel();
        VendorRevenueDTO revenueDTO = new VendorRevenueDTO();

        PaymentMethod cashMethod = paymentMethodRepository.findByPaymentMethodName("Cash");
        PaymentMethod khaltiMethod = paymentMethodRepository.findByPaymentMethodName("KHALTI");

        revenueDTO.setCashOnArrival(bookingDAO.getTotalCashOnArrivalRevenue(hotel.getHotelId(), cashMethod));
        revenueDTO.setKhaltiPayment(bookingDAO.getTotalKhaltiRevenue(hotel.getHotelId(), khaltiMethod));

        return revenueDTO;
    }

    private Hotel getCurrentVendorHotel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User vendor = userRepository.findByUserName(username);
        return hotelRepository.findByUser(vendor);
    }
}