package com.fyp.hotel.dto;

import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.model.PaymentMethod;
import com.fyp.hotel.model.Status;
import com.fyp.hotel.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
public class BookingDTO {

    private Long bookingId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private LocalDate bookingDate;

    private Status status;

    private Long totalAmount;

    private User user;

    private HotelRoom hotelRoom;

    private String paymentMethod;

    private Instant createdAt;

    public BookingDTO(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate, LocalDate bookingDate, Status status, Long totalAmount, User user, HotelRoom hotelRoom, String paymentMethod, Instant createdAt) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingDate = bookingDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.user = user;
        this.hotelRoom = hotelRoom;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
    }

}