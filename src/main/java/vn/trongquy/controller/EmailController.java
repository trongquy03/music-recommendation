package vn.trongquy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.trongquy.service.EmailService;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "Email-Controller")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestParam String email) {
        String code = generateRandomCode();
        emailService.sendVerificationEmail(email, "Mã xác thực", "Mã xác thực của bạn là: " + code);
        return ResponseEntity.ok("Đã gửi mã xác thực đến email.");
    }

    private String generateRandomCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000); // Random 6 chữ số
    }

}
