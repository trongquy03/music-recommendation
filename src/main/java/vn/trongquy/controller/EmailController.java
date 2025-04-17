package vn.trongquy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.trongquy.service.EmailService;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "Email-Controller")
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/send-email")
    public void setEmail(@RequestParam String to,@RequestParam String subject,@RequestParam String content) {
        log.info("sending Email to " + to);
        emailService.sendEmail(to, subject, content);
        log.info("Email sent");
    }
}
