package com.fyp.hotel.dao.vendor;

import com.fyp.hotel.model.HotelRoom;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class VendorDAO {
    private SessionFactory sessionFactory;
    @Autowired
    public VendorDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
//    extract all the room details and room images of specific room
    public HotelRoom getAllRoomsDetails(
            Long roomId
    ) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        TypedQuery<HotelRoom> query = session.createQuery("FROM HotelRoom hr WHERE hr.roomId = :roomId", HotelRoom.class);
        query.setParameter("roomId", roomId);
        HotelRoom hotelRoom = query.getSingleResult(); // getSingleResult is used to get the single result from the database
        transaction.commit();
        session.close();
        return hotelRoom;
    }
    //extract all the room details of all rooms including room images
    public HotelRoom getAllRoomsDetails(
    ) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        TypedQuery<HotelRoom> query = session.createQuery("FROM HotelRoom hr", HotelRoom.class);
        HotelRoom hotelRoom = query.getSingleResult(); // getSingleResult is used to get the single result from the database
        transaction.commit();
        session.close();
        return hotelRoom;
    }



}
