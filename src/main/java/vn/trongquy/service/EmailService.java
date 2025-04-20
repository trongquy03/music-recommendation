package vn.trongquy.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.trongquy.model.EmailVerification;
import vn.trongquy.model.UserEntity;
import vn.trongquy.repository.EmailVerificationRepository;
import vn.trongquy.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Email Service")
public class EmailService {

    private final UserRepository userRepository;

    private final EmailVerificationRepository emailVerificationRepository;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.default-from}")
    private String from;

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    @Value("${brevo.template.verification.id}")
    private int templateId;

    @Value("${brevo.verification.link}")
    private String verificationLink;

    private final RestTemplate restTemplate = new RestTemplate();

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

    public void sendVerificationMail(String toEmail) {
        String url = "https://api.brevo.com/v3/smtp/email";

//        String secretCode = String.format("?secretCode=%s", UUID.randomUUID());
        String secretCode = UUID.randomUUID().toString();
        String fullLink = verificationLink.contains("?")
                ? verificationLink + "&secretCode=" + secretCode
                : verificationLink + "?secretCode=" + secretCode;

        UserEntity user = userRepository.findByEmail(toEmail);

        if (user != null) {
            log.error("User already exists for email: {}", toEmail);
            return;
        }

        // Tạo đối tượng EmailVerification và lưu vào DB
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setUser(user);
        emailVerification.setVerificationCode(secretCode);
        emailVerificationRepository.save(emailVerification);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        // Params định nghĩa trong template Brevo
        Map<String, Object> params = new HashMap<>();
        params.put("EMAIL", toEmail);
        params.put("LINK", fullLink);
//        params.put("LINK", verificationLink + secretCode);

        Map<String, Object> body = new HashMap<>();
        body.put("to", List.of(Map.of("email", toEmail)));
        body.put("sender", Map.of("name", senderName, "email", senderEmail));
        body.put("templateId", templateId);
        body.put("params", params);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            log.info("Email sent: {}", response.getBody());
        } catch (Exception e) {
            log.error("Error sending email", e);
        }
    }
}
