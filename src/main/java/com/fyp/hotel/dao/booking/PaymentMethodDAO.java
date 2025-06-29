package com.fyp.hotel.dao.booking;

import com.fyp.hotel.model.PaymentMethod;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentMethodDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public PaymentMethod getPaymentMethod(String paymentMethod) {
        TypedQuery<PaymentMethod> query = entityManager.createQuery(
                "FROM PaymentMethod pm WHERE pm.paymentMethodName = :paymentMethod",
                PaymentMethod.class);
        query.setParameter("paymentMethod", paymentMethod);

        return query.getSingleResult();
    }
}