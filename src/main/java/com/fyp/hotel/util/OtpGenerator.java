package com.fyp.hotel.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpGenerator {
    public String generateOTP() {

        Random random = new Random();

        // Generate a random 4-digit number
        int otp = 1000 + random.nextInt(9000); // Generates a number between 1000 and 9999

        return String.valueOf(otp); //string.valueOf() converts the integer to a string
    }

}
