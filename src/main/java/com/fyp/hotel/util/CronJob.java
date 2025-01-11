package com.fyp.hotel.util;

import com.fyp.hotel.dao.booking.BookingDAO;
import com.fyp.hotel.dao.hotel.HotelRoomDAO;
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

        List<Booking> bookings = bookingDAO.checkOutDateMatchesCurrentDate();
        if (bookings != null && !bookings.isEmpty()) {
            for (Booking booking : bookings) {
                hotelRoomDAO.updateRoomStatusAsAvailable(booking.getBookingId());
                }
        }
    }
}
