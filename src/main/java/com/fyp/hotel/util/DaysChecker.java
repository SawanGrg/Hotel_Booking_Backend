package com.fyp.hotel.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class DaysChecker {

    public long getDays(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate.equals(checkOutDate)) {
            return 1;
        } else {
            // Calculate the days between checkInDate and checkOutDate
            return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
    }
}
