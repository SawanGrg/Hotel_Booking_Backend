package com.fyp.hotel.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpGenerator {
    public String generateOTP() {
        // The string that will be used to generate the OTP
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        // Seed the random number generator with the hash code of the input string
        Random random = new Random(input.hashCode());

        // Generate a random 4-digit number
        int otp = 1000 + random.nextInt(9000); // Generates a number between 1000 and 9999

        return String.valueOf(otp); //string.valueOf() converts the integer to a string
    }

}
