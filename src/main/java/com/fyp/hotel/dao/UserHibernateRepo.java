package com.fyp.hotel.dao;

import com.fyp.hotel.model.User;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserHibernateRepo {

    @Autowired
    private SessionFactory sessionFactory;


    //    extract user details based on username
    public User getUserByUsername(String username) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        //typed query method is used to get the data from the database
        //alternative of typed query is query method
        TypedQuery<User> query = session.createQuery("FROM User u WHERE u.userName = :username", User.class);
        query.setParameter("username", username);
        User user = query.getSingleResult(); // Use getSingleResult directly
        session.close();
        return user;
    }


}
