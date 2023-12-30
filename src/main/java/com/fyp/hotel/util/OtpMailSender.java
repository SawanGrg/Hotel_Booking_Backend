package com.fyp.hotel.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OtpMailSender {
    public Map<String, String> getVerifyEmailMessage(String otp){

        Map<String,String> verifyEmail= new HashMap<>();

        String subject = "OTP for Account Verification";

        String message = "Hi there,\n" +
                "\n" +
                "Your OTP: "+otp+"\n" +
                "\n" +
                "Use it to complete your registration!\n" +
                "\n" +
                "Enjoy your stay at Annapurna Hotel!\n" +
                "\n" +
                "Best regards,\n" +
                "Annapurna Hotel Booking System";

        verifyEmail.put("subject", subject);
        verifyEmail.put("message", message);

        return verifyEmail;
    }
}
