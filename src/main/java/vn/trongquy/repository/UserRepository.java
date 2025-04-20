package vn.trongquy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.trongquy.common.UserStatus;
import vn.trongquy.model.UserEntity;



public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    UserEntity findByEmail(String email);


    @Query("SELECT u FROM UserEntity u WHERE u.status = 'ACTIVE' " +
            "AND (LOWER(u.username) LIKE :keyword) " +
            "OR LOWER(u.phone) LIKE :keyword " +
            "OR LOWER(u.email) LIKE :keyword")
    Page<UserEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);


}
