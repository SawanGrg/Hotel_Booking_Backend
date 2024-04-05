package com.fyp.hotel.dto.userDto;

import com.fyp.hotel.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingStatusDTO {

    private long bookingId;
    private LocalDate bookingDate;
    private String bookingStatus;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private long totalAmount;
    private long roomId;

    public BookingStatusDTO() {
    }

    public BookingStatusDTO(long bookingId, LocalDate bookingDate, Status bookingStatus, LocalDate checkInDate, LocalDate checkOutDate, long totalAmount, long roomId) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.bookingStatus = bookingStatus.toString();
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.roomId = roomId;
    }
}

