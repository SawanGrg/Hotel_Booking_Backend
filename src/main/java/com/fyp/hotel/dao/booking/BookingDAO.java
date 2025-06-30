package com.fyp.hotel.dao.booking;

import com.fyp.hotel.dto.booking.BookingStatusDTO;
import com.fyp.hotel.model.Booking;
import com.fyp.hotel.model.PaymentMethod;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class BookingDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<Booking> checkOutDateMatchesCurrentDate() {
        String hql = "FROM Booking b WHERE b.checkOutDate = :checkOutDate";
        TypedQuery<Booking> query = entityManager.createQuery(hql, Booking.class);
        query.setParameter("checkOutDate", LocalDate.now());
        return query.getResultList();
    }

    @Transactional
    public List<BookingStatusDTO> getBookingStatusDetails(long hotelId) {
        String hql = "SELECT new com.fyp.hotel.dto.booking.BookingStatusDTO(b.bookingId, b.bookingDate, b.status, b.checkInDate, b.checkOutDate, b.totalAmount, b.hotelRoom.roomId) " +
                "FROM Booking b " +
                "WHERE b.hotelRoom.hotel.hotelId = :hotelId " +
                "AND b.hotelRoom.roomStatus = :status " +
                "AND (b.checkOutDate >= :currentDate OR b.checkOutDate = :currentDate)";

        TypedQuery<BookingStatusDTO> query = entityManager.createQuery(hql, BookingStatusDTO.class);
        query.setParameter("hotelId", hotelId);
        query.setParameter("status", "BOOKED");
        query.setParameter("currentDate", LocalDate.now());

        return query.getResultList();
    }

    @Transactional
    public long getTotalCashOnArrivalRevenue(long hotelId, PaymentMethod paymentMethod) {
        try {
            String hql = "SELECT SUM(b.totalAmount) FROM Booking b " +
                    "WHERE b.hotelRoom.hotel.hotelId = :hotelId " +
                    "AND b.paymentMethod = :paymentMethod " +
                    "AND b.status = 'BOOKED'";

            TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
            query.setParameter("hotelId", hotelId);
            query.setParameter("paymentMethod", paymentMethod);

            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Transactional
    public long getTotalKhaltiRevenue(long hotelId, PaymentMethod paymentMethod) {
        try {
            String hql = "SELECT SUM(b.totalAmount) FROM Booking b " +
                    "WHERE b.hotelRoom.hotel.hotelId = :hotelId " +
                    "AND b.paymentMethod = :paymentMethod " +
                    "AND b.status = 'BOOKED'";

            TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
            query.setParameter("hotelId", hotelId);
            query.setParameter("paymentMethod", paymentMethod);

            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Transactional
    public List<BookingStatusDTO> getBookingStatusDetailsByRoomId(long roomId) {
        String hql = "SELECT new com.fyp.hotel.dto.booking.BookingStatusDTO(b.bookingId, b.bookingDate, b.status, b.checkInDate, b.checkOutDate, b.totalAmount, b.hotelRoom.roomId) " +
                "FROM Booking b " +
                "WHERE b.hotelRoom.roomId = :roomId " +
                "AND b.hotelRoom.roomStatus = :status " +
                "AND (b.checkOutDate >= :currentDate OR b.checkOutDate = :currentDate)";

        TypedQuery<BookingStatusDTO> query = entityManager.createQuery(hql, BookingStatusDTO.class);
        query.setParameter("roomId", roomId);
        query.setParameter("status", "BOOKED");
        query.setParameter("currentDate", LocalDate.now());

        return query.getResultList();
    }
}