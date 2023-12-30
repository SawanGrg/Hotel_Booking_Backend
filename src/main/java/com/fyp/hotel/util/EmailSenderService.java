package com.fyp.hotel.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String receiver, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(receiver);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        // javaMailSender is used to send the email to the user
        javaMailSender.send(simpleMailMessage);
    }
}
