package com.fyp.hotel.dao.booking;

import com.fyp.hotel.model.PaymentMethod;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentMethodDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public PaymentMethodDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Transactional
    public PaymentMethod getPaymentMethod(String paymentMethod) {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query<PaymentMethod> query = session.createQuery("FROM PaymentMethod pm WHERE pm.paymentMethodName = :paymentMethod", PaymentMethod.class);
        query.setParameter("paymentMethod", paymentMethod);

        PaymentMethod paymentMethodObject = query.getSingleResult();

        session.getTransaction().commit();
        session.close();

        return paymentMethodObject;
    }

}
