package com.fyp.hotel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_method")
@RequiredArgsConstructor // generate constructor with all final fields as arguments
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(name = "payment_method_name", nullable = false, length = 50)
    private String paymentMethodName;

    @OneToOne(mappedBy = "paymentMethod", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Booking booking;

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "paymentMethodId=" + paymentMethodId +
                ", paymentMethodName='" + paymentMethodName + '\'' +
                '}';
    }
}
