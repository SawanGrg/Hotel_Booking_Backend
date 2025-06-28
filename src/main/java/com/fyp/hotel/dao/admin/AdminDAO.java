package com.fyp.hotel.dao.admin;

import com.fyp.hotel.dto.BookingDTO;
import com.fyp.hotel.dto.admin.AdminAnalyticsDto;
import com.fyp.hotel.dto.userDto.IssueReportDTO;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.PaymentMethod;
import com.fyp.hotel.model.Role;
import com.fyp.hotel.model.User;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<User> getAllUserFilter(String userName, boolean ascending, int page, int size) {

        StringBuilder query = new StringBuilder("SELECT u FROM User u WHERE 1=1");

//        query.append(" AND u.userStatus = 'VERIFIED'");

        if (!userName.isEmpty()) {
            query.append(" AND u.userName LIKE :userName");
        }

        if (ascending) {
            query.append(" ORDER BY u.userName ASC");
        } else {
            query.append(" ORDER BY u.userName DESC");
        }

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query<User> userQuery = session.createQuery(query.toString(), User.class);

        if(!userName.isEmpty()){
            userQuery.setParameter("userName", "%" + userName + "%");
        }
        List<User> users = userQuery.getResultList();

        session.getTransaction().commit();
        session.close();

        return users;
    }

    public List<User> getAllVendorFilter(String userName, boolean ascending, int page, int size) {

        StringBuilder query = new StringBuilder("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.roleName = 'ROLE_VENDOR' AND 1=1");

        if (!userName.isEmpty()) {
            query.append(" AND u.userName LIKE :userName");
        }

        if (ascending) {
            query.append(" ORDER BY u.userName ASC");
        } else {
            query.append(" ORDER BY u.userName DESC");
        }

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query<User> userQuery = session.createQuery(query.toString(), User.class);

        if(!userName.isEmpty()){
            userQuery.setParameter("userName", "%" + userName + "%");
        }
        List<User> users = userQuery.getResultList();

        session.getTransaction().commit();
        session.close();

        List<User> filteredUsers = new ArrayList<>();

        // Filter out users who have the role ROLE_USER
        for (User user : users) {
            for (Role role : user.getRoles()) {
                if ("ROLE_VENDOR".equals(role.getRoleName())) {
                    filteredUsers.add(user);
                    break; // Once we find the ROLE_USER, no need to check further roles for this user
                }
            }
        }

        return filteredUsers;
    }

    @Transactional
    public List<Hotel> getAllActiveHotels(int page, int size) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            Query<Hotel> query = session.createQuery("SELECT h FROM Hotel h WHERE h.isDeleted = false", Hotel.class);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    @Transactional()
    public long getTotalUsers() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.roleName = 'ROLE_USER'", Long.class);
        long totalUsers = query.getSingleResult();

        session.getTransaction().commit();
        session.close();

        return totalUsers;
    }

    @Transactional()
    public long getTotalVendors() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.roleName = 'ROLE_VENDOR'", Long.class);
        long totalVendors = query.getSingleResult();

        session.getTransaction().commit();
        session.close();

        return totalVendors;
    }

    @Transactional()
    public long getTotalBookings() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Booking b WHERE b.status = 'BOOKED'", Long.class);
        long totalBookings = query.getSingleResult();

        session.getTransaction().commit();
        session.close();

        return totalBookings;
    }

    @Transactional()
    public long getTotalHotels() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query<Long> query = session.createQuery("SELECT COUNT(h) FROM Hotel h", Long.class);
        long totalHotels = query.getSingleResult();

        session.getTransaction().commit();
        session.close();

        return totalHotels;
    }

    @Transactional()
    public long getTotalRooms() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query<Long> query = session.createQuery("SELECT COUNT(hr) FROM HotelRoom hr", Long.class);
        long totalRooms = query.getSingleResult();

        session.getTransaction().commit();
        session.close();

        return totalRooms;
    }

    @Transactional
    public List<IssueReportDTO> getAllReports() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        String query = "SELECT new com.fyp.hotel.dto.userDto.IssueReportDTO(r.title, r.description, u, r.status, h, r.createdAt) " +
                "FROM Report r " +
                "JOIN r.user u " +
                "JOIN u.hotel h";

        List<IssueReportDTO> issueReportDTOs = session.createQuery(query, IssueReportDTO.class).getResultList();

        session.getTransaction().commit();
        session.close();

        return issueReportDTOs;
    }

    @Transactional
    public List<BookingDTO> getUserBookings(long userId, int page, int size) {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query<BookingDTO> query = session.createQuery("SELECT " +
                        "new com.fyp.hotel.dto.BookingDTO" +
                        "(b.bookingId, b.checkInDate, b.checkOutDate, b.bookingDate, b.status, b.totalAmount, b.user, b.hotelRoom, b.paymentMethod, b.createdAt) " +
                "FROM Booking b " +
                "WHERE b.user.userId = :userId",
                BookingDTO.class);
        query.setParameter("userId", userId);

        List<BookingDTO> bookings = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return bookings;
    }

    //get all the revenue from the bookings that are paid by cash on arrival
    @Transactional
    public long getTotalCashOnArrivalRevenue() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT SUM(b.totalAmount) FROM Booking b WHERE b.paymentMethod.paymentMethodName = 'Cash' AND b.status = 'BOOKED'";
            Query query = session.createQuery(hql);
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

    //get all the revenue from the bookings that are paid by Khalti
    @Transactional
    public long getTotalKhaltiRevenue(){
            Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT SUM(b.totalAmount) FROM Booking b WHERE b.paymentMethod.paymentMethodName = 'KHALTI' AND b.status = 'BOOKED'";
            Query query = session.createQuery(hql);
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
    public User getUserProfile(long userId) {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        User user = session.get(User.class, userId);

        session.getTransaction().commit();
        session.close();

        return user;
    }
}
