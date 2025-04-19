package vn.trongquy.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Email Service")
public class EmailService {

    private final JavaMailSender mailSender;

//    @Value("${spring.mail.username}")
//    private String from;
    @Value("${spring.mail.properties.mail.default-from}")
    private String from;

    public boolean sendVerificationEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);

            System.out.println("Email sent to: " + toEmail);
            return true;
        } catch (MailException ex) {
            System.err.println("Failed to send email to: " + toEmail);
            ex.printStackTrace(); // hoặc dùng logger.error(...)
            return false;
        }

    }
}
