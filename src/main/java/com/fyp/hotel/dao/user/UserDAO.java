package com.fyp.hotel.dao.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Boolean isUserExist(String username) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(*) FROM User u WHERE u.userName = :username",
                Long.class);
        query.setParameter("username", username);

        Long count = query.getSingleResult();
        return count > 0;
    }
}