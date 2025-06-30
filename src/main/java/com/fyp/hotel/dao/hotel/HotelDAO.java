package com.fyp.hotel.dao.hotel;

import com.fyp.hotel.dto.hotel.DisplayHotelWithAmenitiesDto;
import com.fyp.hotel.dto.hotel.ReviewDTO;
import com.fyp.hotel.model.Booking;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.enums.Status;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
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
                    "AND (b.checkOutDate >= :currentDate OR b.checkOutDate = :currentDate) ";

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


            // Add pagination parameters
            Query<Booking> query = sessionObj.createQuery(hql.toString(), Booking.class);
            query.setParameter("hotelId", hotelId);


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
    //get all hotel with amenities from hotel room table and rating from rating table
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
                    "hr.hasWifi, hr.hasRefridge, hr.hasAC, hr.hasTV, hr.hasBalcony, h.hotelStar) " +
                    "from Hotel h " +
                    "join HotelRoom hr on h.hotelId = hr.hotel.hotelId " +
                    "where h.isDeleted = false"; // Add condition for isDeleted

            // Adding filter conditions based on provided parameters
            if (hotelName != null && !hotelName.isEmpty()) {
                hql += " and h.hotelName like :hotelName";
            }

            if (hotelAddress != null && !hotelAddress.isEmpty()) {
                hql += " and h.hotelAddress like :hotelAddress";
            }

            hql += " group by h.hotelId"; // group by is used to group the result by hotel id

            Query<DisplayHotelWithAmenitiesDto> query = sessionObj.createQuery(hql, DisplayHotelWithAmenitiesDto.class);

            // Setting parameters if provided
            if (hotelName != null && !hotelName.isEmpty()) {
                query.setParameter("hotelName", "%" + hotelName + "%");
            }

            if (hotelAddress != null && !hotelAddress.isEmpty()) {
                query.setParameter("hotelAddress", "%" + hotelAddress + "%");
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

            String hql = "UPDATE Booking b SET b.status = :status, b.vendorUpdated = true WHERE b.bookingId = :bookingId";

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

    //extract all the hotel review details based on the hotel id
    public List<ReviewDTO> getHotelReviews(long hotelId){
        Session session = null;
        try{
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT new com.fyp.hotel.dto.vendorDto.ReviewDTO(r.reviewId, r.reviewContent, r.hotel.hotelId, r.user, r.createdDate) FROM Review r WHERE r.hotel.hotelId = :hotelId";

            Query<ReviewDTO> query = session.createQuery(hql, ReviewDTO.class);
            query.setParameter("hotelId", hotelId);

            List<ReviewDTO> reviews = query.getResultList();
            return reviews;

        }catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //extract hotel owner user name from the hotel id
    public String getHotelOwnerName(long hotelId){
        Session session = null;
        try{
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT h.user.userName FROM Hotel h WHERE h.hotelId = :hotelId";

            Query<String> query = session.createQuery(hql, String.class);
            query.setParameter("hotelId", hotelId);

            String ownerName = query.getSingleResult();
            return ownerName;

        }catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return "";
        }
        finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //save hotel details to the database
    @Transactional
    public Hotel saveHotel(Hotel hotel){
        Session session = null;
        try{
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            session.save(hotel);

            session.getTransaction().commit();
            return hotel;
        }catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        }
        finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Hotel getHotelWithRoomsByUserId(long userId) {
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT h FROM Hotel h WHERE h.user.userId = :userId";

            Query<Hotel> query = session.createQuery(hql, Hotel.class);
            query.setParameter("userId", userId);

            Hotel hotel = query.getSingleResult();

            session.getTransaction().commit();
            return hotel;
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Transactional
    public Hotel updateHotel(Hotel hotel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // Create HQL update query
            String hql = "UPDATE Hotel SET hotelName = :hotelName, hotelAddress = :hotelAddress, " +
                    "hotelContact = :hotelContact, hotelDescription = :hotelDescription, " +
                    "hotelEmail = :hotelEmail, hotelImage = :hotelImage, hotelPan = :hotelPan, " +
                    "hotelStar = :hotelStar, hotelStatus = :hotelStatus, isDeleted = :isDeleted, " +
                    "updatedAt = :updatedAt WHERE hotelId = :hotelId";

            Query query = session.createQuery(hql);
            query.setParameter("hotelName", hotel.getHotelName());
            query.setParameter("hotelAddress", hotel.getHotelAddress());
            query.setParameter("hotelContact", hotel.getHotelContact());
            query.setParameter("hotelDescription", hotel.getHotelDescription());
            query.setParameter("hotelEmail", hotel.getHotelEmail());
            query.setParameter("hotelImage", hotel.getHotelImage());
            query.setParameter("hotelPan", hotel.getHotelPan());
            query.setParameter("hotelStar", hotel.getHotelStar());
            query.setParameter("hotelStatus", hotel.getHotelStatus());
            query.setParameter("isDeleted", hotel.getIsDeleted());
            query.setParameter("updatedAt", Instant.now());
            query.setParameter("hotelId", hotel.getHotelId());

            // Execute the update query
            int updatedEntities = query.executeUpdate();

            // Commit the transaction
            session.getTransaction().commit();

            // Check if any entities were updated
            if (updatedEntities > 0) {
                return hotel;
            } else {
                return null; // Hotel not found or not updated
            }
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // Return null to indicate an error occurred
            return null;
        }
    }

    @Transactional
    public Boolean existByHotelPan(String hotelPan){
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT COUNT(h.hotelId) FROM Hotel h WHERE h.hotelPan = :hotelPan";

            Query query = session.createQuery(hql);
            query.setParameter("hotelPan", hotelPan);

            long count = (long) query.getSingleResult();

            session.getTransaction().commit();
            return count > 0;
        }catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //Check if the room is already exist or not based on the hotel id
    @Transactional
    public Boolean RoomExistOrNot(String roomNumber, long hotelId){
        Session session = null;
        try {
            session = this.sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT COUNT(hr.roomId) FROM HotelRoom hr WHERE hr.roomNumber = :roomNumber AND hr.hotel.hotelId = :hotelId";

            Query query = session.createQuery(hql);
            query.setParameter("roomNumber", roomNumber);
            query.setParameter("hotelId", hotelId);

            long count = (long) query.getSingleResult();

            session.getTransaction().commit();
            return count > 0;
        }catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}