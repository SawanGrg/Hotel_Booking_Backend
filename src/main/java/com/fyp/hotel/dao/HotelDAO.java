package com.fyp.hotel.dao;

import com.fyp.hotel.dto.CheckRoomAvailabilityDto;
import com.fyp.hotel.model.Booking;
import com.fyp.hotel.model.Hotel;
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
public class HotelDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public HotelDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Hotel> getHotelsBasedOnCondition(
            String hotelName,
            String hotelAddress,
            int page,
            int size
    ){
        Session sessionObj = this.sessionFactory.openSession();
        sessionObj.beginTransaction();

        StringBuilder query = new StringBuilder("from Hotel h where 1=1");

        if(!hotelAddress.isEmpty()){
            query.append(" and h.hotelAddress like :hotelAddress");
        }

        if(!hotelName.isEmpty()){
            query.append(" and h.hotelName like :hotelName");
        }

        Query<Hotel> hotelQuery = sessionObj.createQuery(query.toString(), Hotel.class);

        if(!hotelAddress.isEmpty()){
            hotelQuery.setParameter("hotelAddress", "%" + hotelAddress + "%");
        }

        if(!hotelName.isEmpty()){
            hotelQuery.setParameter("hotelName", "%" + hotelName + "%");
        }

//        hotelQuery.setFirstResult((page - 1) * size);
//        hotelQuery.setMaxResults(size);

        List<Hotel> hotels = hotelQuery.getResultList();

        sessionObj.getTransaction().commit();
        sessionObj.close();

        return hotels;
    }

    //check hotel room availability
    @Transactional
    public List<Booking> getBookingsForToday(long roomId) {
        Session sessionObj = null;
        try {
            sessionObj = this.sessionFactory.openSession();
            sessionObj.beginTransaction();

            LocalDate currentDate = LocalDate.now();

            String hql = "SELECT b FROM Booking b " +
                    "LEFT JOIN FETCH b.user " +
                    "WHERE b.hotelRoom.roomId = :roomId " +
                    "AND :currentDate BETWEEN b.checkInDate AND b.checkOutDate";

            Query<Booking> query = sessionObj.createQuery(hql, Booking.class);

            query.setParameter("roomId", roomId);
            query.setParameter("currentDate", currentDate);

            List<Booking> bookings = query.getResultList();

            sessionObj.getTransaction().commit();

            return bookings;

        } catch (Exception e) {
            if (sessionObj != null) {
                sessionObj.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (sessionObj != null) {
                sessionObj.close();
            }
        }
    }

    //check from payment and booking table if the room is booked or refunded or cancelled
    @Transactional
    public List<Booking> getBookingDetails(String status, Long hotelId) {
        Session sessionObj = null;
        try {
            sessionObj = this.sessionFactory.openSession();
            sessionObj.beginTransaction();

            StringBuilder hql = new StringBuilder("SELECT b FROM Booking b ");
            hql.append("JOIN b.hotelRoom hr ");
            hql.append("JOIN hr.hotel h ");
            hql.append("WHERE h.hotelId = :hotelId ");

            if (status != null && !status.isEmpty()) {
                hql.append("AND b.bookingStatus = :status");
            }

            Query<Booking> query = sessionObj.createQuery(hql.toString(), Booking.class);
            query.setParameter("hotelId", hotelId);

            if (status != null && !status.isEmpty()) {
                query.setParameter("status", status);
            }

            List<Booking> bookings = query.getResultList();

            return bookings;
        } catch (Exception e) {
            if (sessionObj != null) {
                sessionObj.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (sessionObj != null) {
                sessionObj.close();
            }
        }
    }

}
