package vn.trongquy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.trongquy.model.EmailVerification;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByVerificationCode(String verificationCode);
}
