package com.fyp.hotel.model;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@RequiredArgsConstructor // generate constructor with all final fields as arguments
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto means auto increment by 1 it will be in sequence and it will be unique
    @Column(name = "payment_id")
    private long paymentId;

    @Column(name = "payment_status", nullable = false, length = 50)
    private String paymentStatus;

    @Column(name = "payment_amount", nullable = false, length = 50)
    private long paymentAmount;

    @Column(name = "payment_date", nullable = false, length = 50)
    private LocalDate paymentDate;

    @Column(name = "transaction_id", nullable = false, length = 50)
    private String transactionId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "booking_id",
            nullable = false,
            referencedColumnName = "booking_id"
    )
    private Booking booking;

    @Column(name = "created_at", nullable = false, length = 50)
    private Instant createdAt;

    @Column(name = "updated_at", length = 50)
    private Instant updatedAt;

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentAmount=" + paymentAmount +
                ", paymentDate='" + paymentDate + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", booking=" + booking +
                '}';
    }

}
