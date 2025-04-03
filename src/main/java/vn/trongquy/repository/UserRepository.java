package vn.trongquy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.trongquy.model.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}
