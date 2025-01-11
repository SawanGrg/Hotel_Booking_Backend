package com.fyp.hotel.dto;

import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.model.PaymentMethod;
import com.fyp.hotel.enums.Status;
import com.fyp.hotel.model.User;
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

    private String hotelRoomName;

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

    public BookingDTO(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate, LocalDate bookingDate, Status status, Long totalAmount, User user, HotelRoom hotelRoom, PaymentMethod paymentMethod, Instant createdAt) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingDate = bookingDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.user = user;
        this.hotelRoomName = hotelRoom.getRoomNumber();
        this.paymentMethod = paymentMethod.getPaymentMethodName();
        this.createdAt = createdAt;
    }


}
