package com.fyp.hotel.repository;

import com.fyp.hotel.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    PaymentMethod findByPaymentMethodName(String paymentMethodName);
}
