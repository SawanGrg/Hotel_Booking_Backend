package com.fyp.hotel.dao.admin;

import com.fyp.hotel.dto.booking.BookingDTO;
import com.fyp.hotel.dto.user.IssueReportDTO;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.Role;
import com.fyp.hotel.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> getAllUserFilter(String userName, boolean ascending, int page, int size) {
        StringBuilder query = new StringBuilder("SELECT u FROM User u WHERE 1=1");

        if (!userName.isEmpty()) {
            query.append(" AND u.userName LIKE :userName");
        }

        if (ascending) {
            query.append(" ORDER BY u.userName ASC");
        } else {
            query.append(" ORDER BY u.userName DESC");
        }

        TypedQuery<User> userQuery = entityManager.createQuery(query.toString(), User.class);

        if(!userName.isEmpty()) {
            userQuery.setParameter("userName", "%" + userName + "%");
        }

        return userQuery.getResultList();
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

        TypedQuery<User> userQuery = entityManager.createQuery(query.toString(), User.class);

        if(!userName.isEmpty()) {
            userQuery.setParameter("userName", "%" + userName + "%");
        }

        List<User> users = userQuery.getResultList();
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
        TypedQuery<Hotel> query = entityManager.createQuery(
                "SELECT h FROM Hotel h WHERE h.isDeleted = false", Hotel.class);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Transactional
    public long getTotalUsers() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.roleName = 'ROLE_USER'", Long.class);
        return query.getSingleResult();
    }

    @Transactional
    public long getTotalVendors() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.roleName = 'ROLE_VENDOR'", Long.class);
        return query.getSingleResult();
    }

    @Transactional
    public long getTotalBookings() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(b) FROM Booking b WHERE b.status = 'BOOKED'", Long.class);
        return query.getSingleResult();
    }

    @Transactional
    public long getTotalHotels() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(h) FROM Hotel h", Long.class);
        return query.getSingleResult();
    }

    @Transactional
    public long getTotalRooms() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(hr) FROM HotelRoom hr", Long.class);
        return query.getSingleResult();
    }

    @Transactional
    public List<IssueReportDTO> getAllReports() {
        String query = "SELECT new com.fyp.hotel.dto.user.IssueReportDTO(r.title, r.description, u, r.status, h, r.createdAt) " +
                "FROM Report r " +
                "JOIN r.user u " +
                "JOIN u.hotel h";

        return entityManager.createQuery(query, IssueReportDTO.class).getResultList();
    }

    @Transactional
    public List<BookingDTO> getUserBookings(long userId, int page, int size) {
        TypedQuery<BookingDTO> query = entityManager.createQuery(
                "SELECT new com.fyp.hotel.dto.BookingDTO" +
                        "(b.bookingId, b.checkInDate, b.checkOutDate, b.bookingDate, b.status, b.totalAmount, b.user, b.hotelRoom, b.paymentMethod, b.createdAt) " +
                        "FROM Booking b " +
                        "WHERE b.user.userId = :userId", BookingDTO.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    @Transactional
    public long getTotalCashOnArrivalRevenue() {
        try {
            String hql = "SELECT SUM(b.totalAmount) FROM Booking b WHERE b.paymentMethod.paymentMethodName = 'Cash' AND b.status = 'BOOKED'";
            TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Transactional
    public long getTotalKhaltiRevenue() {
        try {
            String hql = "SELECT SUM(b.totalAmount) FROM Booking b WHERE b.paymentMethod.paymentMethodName = 'KHALTI' AND b.status = 'BOOKED'";
            TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Transactional
    public User getUserProfile(long userId) {
        return entityManager.find(User.class, userId);
    }
}