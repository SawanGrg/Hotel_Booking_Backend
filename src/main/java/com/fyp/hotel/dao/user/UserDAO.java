package com.fyp.hotel.dao.user;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    //true if user exist
    @Transactional
    public Boolean isUserExist(String username) {
        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();

            Long count = (Long) session.createQuery("SELECT COUNT(*) FROM User u WHERE u.userName = :username")
                    .setParameter("username", username)
                    .uniqueResult();

            session.getTransaction().commit();
            return count > 0;

        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }
}
