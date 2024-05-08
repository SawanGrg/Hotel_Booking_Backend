package com.fyp.hotel.util;

import com.fyp.hotel.dao.BookingDAO;
import com.fyp.hotel.dao.HotelRoomDAO;
import com.fyp.hotel.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CronJob {

    @Autowired
    private BookingDAO bookingDAO;
    @Autowired
    private HotelRoomDAO hotelRoomDAO;

    @Scheduled(cron = "59 * * * * *")
    public void cronJob() {
        System.out.println("Cron job is running");

        // Check if the checkout date matches the current date
        List<Booking> bookings = bookingDAO.checkOutDateMatchesCurrentDate();
        if (bookings != null && !bookings.isEmpty()) {
            System.out.println("Checkout date matches the current date");
            for (Booking booking : bookings) {
                System.out.println("Booking ID: " + booking.getBookingId());

                //based on those booking id we extract the hotel room if and mark it as available
                hotelRoomDAO.updateRoomStatusAsAvailable(booking.getBookingId());
                System.out.println("Room status updated as available");
                }
        } else {
            System.out.println("No bookings found with checkout date matching the current date");
        }
    }
}
