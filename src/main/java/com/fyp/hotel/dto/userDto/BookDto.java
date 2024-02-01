package com.fyp.hotel.dto.userDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    @NotNull(message = "User Id cannot be null")
    @Length(min = 1, message = "User Id cannot be null")
    private Long roomId;

    @NotNull(message = "Check In Date cannot be null")
    @Length(min = 1, message = "Check In Date cannot be null")
    private LocalDate checkInDate;

    @NotNull(message = "Check Out Date cannot be null")
    @Length(min = 1, message = "Check Out Date cannot be null")
    private LocalDate checkOutDate;

    @NotNull(message = "Booking Date cannot be null")
    @Length(min = 1, message = "Booking Date cannot be null")
    private LocalDate bookingDate;

    @NotNull(message = "number of guest cannot be null")
    private Long numberOfGuest;

    @NotNull(message = "Payment Method cannot be null")
    @Length(min = 1, message = "Payment Method cannot be null")
    private String paymentMethod;

    @NotNull(message = "Total Amount cannot be null")
    private Instant createdAt;

}
