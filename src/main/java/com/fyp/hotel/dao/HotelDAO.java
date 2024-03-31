package com.fyp.hotel.dao;

import com.fyp.hotel.dto.CheckRoomAvailabilityDto;
import com.fyp.hotel.dto.DisplayHotelWithAmenitiesDto;
import com.fyp.hotel.model.Booking;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.Status;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    @Transactional
    public List<Booking> getBookingDetails(String status, Long hotelId, int page, int size) {
        Session sessionObj = null;
        try {
            sessionObj = this.sessionFactory.openSession();
            sessionObj.beginTransaction();

            StringBuilder hql = new StringBuilder("SELECT b FROM Booking b ");
            hql.append("JOIN FETCH b.hotelRoom hr "); // Fetch complete HotelRoom entity
            hql.append("JOIN hr.hotel h ");
            hql.append("JOIN FETCH b.user ");
            hql.append("WHERE h.hotelId = :hotelId ");

            if (status != null && !status.isEmpty()) {
                hql.append("AND b.status = :status ");
            }

            // Add pagination parameters
            Query<Booking> query = sessionObj.createQuery(hql.toString(), Booking.class);
            query.setParameter("hotelId", hotelId);

            if (status != null && !status.isEmpty()) {
                query.setParameter("status", status);
            }

            // Set pagination parameters
            if (page >= 0 && size > 0) {
                query.setFirstResult(page * size);
                query.setMaxResults(size);
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

    //get all hotel with amenites from hotel room table and rating from rating table
    public List<DisplayHotelWithAmenitiesDto> getHotelWithAmenitiesAndRating(
            String hotelName,
            String hotelAddress
    ) {
        Session sessionObj = null;
        try {
            sessionObj = this.sessionFactory.openSession();
            sessionObj.beginTransaction();

            String hql = "select new com.fyp.hotel.dto.DisplayHotelWithAmenitiesDto(" +
                    "h.hotelId, h.hotelName, h.hotelContact, h.hotelAddress, h.hotelEmail, " +
                    "h.hotelDescription, h.hotelImage, h.hotelPan, h.hotelStatus, " +
                    "hr.hasWifi, hr.hasRefridge, hr.hasAC, hr.hasTV, hr.hasBalcony) " +
                    "from Hotel h " +
                    "join HotelRoom hr on h.hotelId = hr.hotel.hotelId";

            // Adding filter conditions based on provided parameters
            if (hotelName != null && !hotelName.isEmpty()) {
                hql += " where h.hotelName like :hotelName";
            }

            if (hotelAddress != null && !hotelAddress.isEmpty()) {
                if (hql.contains("where")) {
                    hql += " and h.hotelAddress like :hotelAddress";
                } else {
                    hql += " where h.hotelAddress like :hotelAddress";
                }
            }

            hql += " group by h.hotelId"; // group by is used to group the result by hotel id

            Query<DisplayHotelWithAmenitiesDto> query = sessionObj.createQuery(hql, DisplayHotelWithAmenitiesDto.class);

            // Setting parameters if provided
            if (!hotelAddress.isEmpty()) {
                query.setParameter("hotelAddress", "%" + hotelAddress + "%");
            }

            if (!hotelName.isEmpty()) {
                query.setParameter("hotelName", "%" + hotelName + "%");
            }

            List<DisplayHotelWithAmenitiesDto> resultList = query.getResultList();

            sessionObj.getTransaction().commit();
            return resultList;
        } catch (Exception e) {
            if (sessionObj != null && sessionObj.getTransaction() != null) {
                sessionObj.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (sessionObj != null) {
                sessionObj.close();
            }
        }
    }

    //change the booking status to message from the param of specific booking

    @Transactional
    public Boolean updateBookingStatus(long bookingId, String status) {
        Session session = null;

        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "UPDATE Booking b SET b.status = :status WHERE b.bookingId = :bookingId";

            Query query = session.createQuery(hql);
            query.setParameter("status", Status.valueOf(status));
            query.setParameter("bookingId", bookingId);

            int updatedRows = query.executeUpdate();

            session.getTransaction().commit();
            return updatedRows > 0;
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace(); // Handle exception appropriately, e.g., log it
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

//    extract total booking details based on the hotel id
    public long getTotalBookings(long hotelId){
            Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT COUNT(b.bookingId) FROM Booking b JOIN b.hotelRoom hr WHERE hr.hotel.hotelId = :hotelId";

            Query query = session.createQuery(hql);
            query.setParameter("hotelId", hotelId);

            long totalBookings = (long) query.getSingleResult();

            session.getTransaction().commit();
            return totalBookings;
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace(); // Handle exception appropriately, e.g., log it
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

//    extract total booking marked as refunded based on the hotel id
    public long getTotalRefundedBookings(long hotelId){
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT COUNT(b.bookingId) FROM Booking b JOIN b.hotelRoom hr WHERE hr.hotel.hotelId = :hotelId AND b.status = :status";

            Query query = session.createQuery(hql);
            query.setParameter("hotelId", hotelId);
            query.setParameter("status", Status.REFUNDED);

            long totalBookings = (long) query.getSingleResult();

            session.getTransaction().commit();
            return totalBookings;
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace(); // Handle exception appropriately, e.g., log it
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    //    extract total booking marked as refunded based on the hotel id
    @Transactional(rollbackOn = Exception.class)
    public long getTotalCancelledsBookings(long hotelId){
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT COUNT(b.bookingId) FROM Booking b JOIN b.hotelRoom hr WHERE hr.hotel.hotelId = :hotelId AND b.status = :status";

            Query query = session.createQuery(hql);
            query.setParameter("hotelId", hotelId);
            query.setParameter("status", Status.CANCELLED);

            long totalBookings = (long) query.getSingleResult();

            session.getTransaction().commit();
            return totalBookings;
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace(); // Handle exception appropriately, e.g., log it
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //extract total pending booking of specific hotel
    @Transactional
    public long getTotalPendingBookings(long hotelId){
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT COUNT(b.bookingId) FROM Booking b JOIN b.hotelRoom hr WHERE hr.hotel.hotelId = :hotelId AND b.status = :status";

            Query query = session.createQuery(hql);
            query.setParameter("hotelId", hotelId);
            query.setParameter("status", Status.PENDING);

            long totalBookings = (long) query.getSingleResult();

            session.getTransaction().commit();
            return totalBookings;
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace(); // Handle exception appropriately, e.g., log it
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //get total booked rooms based on the hotel id
    @Transactional
    public long getTotalBookedBookings(long hotelId){
        Session session = null;
        try{
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT COUNT(b.bookingId) FROM Booking b JOIN b.hotelRoom hr WHERE hr.hotel.hotelId = :hotelId AND b.status = :status";

            Query query = session.createQuery(hql);
            query.setParameter("hotelId", hotelId);
            query.setParameter("status", Status.BOOKED);

            long totalBookings = (long) query.getSingleResult();

            session.getTransaction().commit();
            return totalBookings;
        }catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        }
        finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Transactional
    public long getHotelTotalRevenue(long hotelId){
        Session session = null;
        try{
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT SUM(b.totalAmount) FROM Booking b JOIN b.hotelRoom hr WHERE hr.hotel.hotelId = :hotelId AND b.status = :status";

            Query query = session.createQuery(hql);
            query.setParameter("hotelId", hotelId);
            query.setParameter("status", Status.BOOKED);

            Long totalRevenue = (Long) query.getSingleResult();

            session.getTransaction().commit();
            return totalRevenue != null ? totalRevenue : 0;
        }catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        }
        finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Transactional
    public long getTodayTotalArrivingRooms(long hotelId, LocalDate today){
        Session session = null;
        try{
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT COUNT(b.bookingId) FROM Booking b JOIN b.hotelRoom hr WHERE hr.hotel.hotelId = :hotelId AND b.checkInDate = :today";

            Query query = session.createQuery(hql);
            query.setParameter("hotelId", hotelId);
            query.setParameter("today", today);

            long totalRooms = (long) query.getSingleResult();

            session.getTransaction().commit();
            return totalRooms;
        }
        catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        }
        finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Transactional
    public long getTomorrowTotalArrivingRooms(long hotelId) {
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            LocalDate tomorrow = LocalDate.now().plusDays(1); // Calculate tomorrow's date

            String hql = "SELECT COUNT(b.bookingId) FROM Booking b JOIN b.hotelRoom hr WHERE hr.hotel.hotelId = :hotelId AND b.checkInDate = :tomorrow";

            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("hotelId", hotelId);
            query.setParameter("tomorrow", tomorrow);

            long totalRooms = query.getSingleResult();

            session.getTransaction().commit();
            return totalRooms;

        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    @Transactional
    public long getTodayAvailableRoom(long hotelId) {
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            // Construct HQL query to count available rooms for today
            String hql = "SELECT COUNT(hr) FROM HotelRoom hr WHERE hr.hotel.hotelId = :hotelId AND hr.roomStatus = 'AVAILABLE'";

            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("hotelId", hotelId);

            long availableRooms = query.getSingleResult();

            session.getTransaction().commit();
            return availableRooms;

        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}