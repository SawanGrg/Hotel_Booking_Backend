package com.fyp.hotel.dao.user;

import com.fyp.hotel.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserHibernateRepo {

    @PersistenceContext
    private EntityManager entityManager;

    public User getUserByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "FROM User u WHERE u.userName = :username",
                User.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }
}