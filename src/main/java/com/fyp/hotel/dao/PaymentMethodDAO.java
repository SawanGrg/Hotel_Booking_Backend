package com.fyp.hotel.dao;

import com.fyp.hotel.model.Payment;
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

    //extract payment id based on payment method name
//    @Transactional
//    public PaymentMethod getPaymentMethodId(String paymentMethod) {
//        // openSession is used to open the session and get the session of the database
//        // which is configured in the application.properties file
//        Session session = this.sessionFactory.openSession();
//        // beginTransaction is used to begin the transaction where we can perform the database operations
//        session.beginTransaction();
//        // getSingleResult is used to get the single result from the database
//        // createQuery is used to create the query to get the data from the database
//        //TypedQuery is used to get the data from the database
//        //alternative of TypedQuery is Query
//        Query<PaymentMethod> query = session.createQuery("SELECT pm.paymentMethodId FROM PaymentMethod pm WHERE pm.paymentMethodName = :paymentMethod", PaymentMethod.class);
//        query.setParameter("paymentMethod", paymentMethod);
//        PaymentMethod paymentMethodObject = query.getSingleResult();
//        // commit is used to commit the transaction to the database
//        //getTransaction is used to get the transaction of the database
//        session.getTransaction().commit();
//        // close is used to close the session of the database
//        session.close();
//        return paymentMethodObject;
//    }

    @Transactional
    public PaymentMethod getPaymentMethod(String paymentMethod) {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Query<PaymentMethod> query = session.createQuery("FROM PaymentMethod pm WHERE pm.paymentMethodName = :paymentMethod", PaymentMethod.class);
        query.setParameter("paymentMethod", paymentMethod);

        PaymentMethod paymentMethodObject = query.getSingleResult();

        //getTransaction is used to get the transaction of the database
        //for commiting the transaction
        //FOR EVERY TRANSACTION WE NEED TO COMMIT THE TRANSACTION
        //for example: if we are inserting the data into the database
        session.getTransaction().commit();
        session.close();

        return paymentMethodObject;
    }

}
