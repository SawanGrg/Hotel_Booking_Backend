package com.fyp.hotel.dao;

import com.fyp.hotel.dto.BookingDTO;
import com.fyp.hotel.dto.userDto.BookingStatusDTO;
import com.fyp.hotel.dto.vendorDto.RoomHistoryDTO;
import com.fyp.hotel.model.Booking;
import com.fyp.hotel.model.PaymentMethod;
import com.fyp.hotel.model.Status;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Booking> checkOutDateMatchesCurrentDate() {
        // Check if the checkout date matches the current date
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "FROM Booking b WHERE b.checkOutDate = :checkOutDate";

            Query<Booking> query = session.createQuery(hql, Booking.class);
            query.setParameter("checkOutDate", LocalDate.now());

            return query.list();

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    //extract those booking details if the hotel room is set as booked and the checkout date is less than the current date
    public List<BookingStatusDTO> getBookingStatusDetails(long hotelId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT new com.fyp.hotel.dto.userDto.BookingStatusDTO(b.bookingId, b.bookingDate, b.status, b.checkInDate, b.checkOutDate, b.totalAmount, b.hotelRoom.roomId) " +
                    "FROM Booking b " +
                    "WHERE b.hotelRoom.hotel.hotelId = :hotelId " + // Add condition for hotel ID
                    "AND b.hotelRoom.roomStatus = :status " + // Add space after :status
                    "AND (b.checkOutDate >= :currentDate OR b.checkOutDate = :currentDate)";

            Query<BookingStatusDTO> query = session.createQuery(hql, BookingStatusDTO.class);

            query.setParameter("hotelId", hotelId);
            query.setParameter("status", "BOOKED");
            query.setParameter("currentDate", LocalDate.now());

            return query.list();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Transactional
    public long getTotalCashOnArrivalRevenue(long hotelId, PaymentMethod paymentMethod) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT SUM(b.totalAmount) FROM Booking b WHERE b.hotelRoom.hotel.hotelId = :hotelId AND b.paymentMethod = :paymentMethod AND b.status = 'BOOKED'";

            Query query = session.createQuery(hql);

            query.setParameter("hotelId", hotelId);
            query.setParameter("paymentMethod", paymentMethod);

            return (long) query.uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Transactional
    public long getTotalKhaltiRevenue(long hotelId, PaymentMethod paymentMethod) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT SUM(b.totalAmount) FROM Booking b WHERE b.hotelRoom.hotel.hotelId = :hotelId AND b.paymentMethod = :paymentMethod AND b.status = 'BOOKED'";

            Query query = session.createQuery(hql);

            query.setParameter("hotelId", hotelId);
            query.setParameter("paymentMethod", paymentMethod);

            return (long) query.uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public List<BookingStatusDTO> getBookingStatusDetailsByRoomId(long roomId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT new com.fyp.hotel.dto.userDto.BookingStatusDTO(b.bookingId, b.bookingDate, b.status, b.checkInDate, b.checkOutDate, b.totalAmount, b.hotelRoom.roomId) " +
                    "FROM Booking b " +
                    "WHERE b.hotelRoom.roomId = :roomId " +
                    "AND b.hotelRoom.roomStatus = :status " +
                    "AND (b.checkOutDate >= :currentDate OR b.checkOutDate = :currentDate)";


            Query<BookingStatusDTO> query = session.createQuery(hql, BookingStatusDTO.class);

            query.setParameter("roomId", (Long) roomId); // Set roomId parameter
            query.setParameter("status", "BOOKED");
            query.setParameter("currentDate", LocalDate.now());

            return query.list();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

//    public List<BookingDTO> getRoomHistory(long roomId) {
//        Session session = null;
//        try {
//            session = sessionFactory.openSession();
//            session.beginTransaction();
//
//            String hql = "SELECT new com.fyp.hotel.dto.BookingDTO(b.bookingId, b.checkInDate, b.checkOutDate, b.totalAmount, b.paymentMethod, b.bookingDate, b.status) " +
//                    "FROM Booking b " +
//                    "WHERE b.hotelRoom.roomId = :roomId ";
//
//            Query<BookingDTO> query = session.createQuery(hql, BookingDTO.class);
//            query.setParameter("roomId", roomId);
//            return query.list();
//
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//    }



}
