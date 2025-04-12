package vn.trongquy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.trongquy.common.UserStatus;
import vn.trongquy.controller.request.UserCreationRequest;
import vn.trongquy.controller.request.UserPasswordRequest;
import vn.trongquy.controller.request.UserUpdateRequest;
import vn.trongquy.controller.response.UserResponse;
import vn.trongquy.exception.ResourceNotFoundException;
import vn.trongquy.model.UserEntity;
import vn.trongquy.repository.UserRepository;
import vn.trongquy.service.UserService;

import java.util.List;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserEntity> findAll(String keyword, String sort, int page, int size) {
        if (StringUtils.hasLength(keyword)) {

        }
        return List.of();
    }

    @Override
    public UserEntity findById(Long id) {

        UserEntity userEntity = getUserEntity(id);

        return UserEntity.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .gender(userEntity.getGender())
                .birthday(userEntity.getBirthday())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .status(userEntity.getStatus())
                .build();
    }

    @Override
    public UserResponse findByUsername(String username) {
        return null;
    }

    @Override
    public UserResponse findByEmail(String email) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserEntity save(UserCreationRequest req) {
//        if (!req.getPhone().isBlank() && userRepository.existsByPhone(req.getPhone())) {
//            throw new DataIntegrityViolationException("Phone number already exists");
//        }
        log.info("Saving user {}", req);
        UserEntity user = UserEntity.builder()
                .username(req.getUsername())
                .gender(req.getGender())
                .birthday(req.getBirthday())
                .email(req.getEmail())
                .phone(req.getPhone())
                .type(req.getType())
                .status(UserStatus.NONE)
                .build();
        return userRepository.save(user);
    }

    @Override
    public UserEntity update(Long id,UserUpdateRequest req) {
        log.info("Updating user {}", req);
        UserEntity user = getUserEntity(id);
        user.setUsername(req.getUsername());
        user.setGender(req.getGender());
        user.setBirthday(req.getBirthday());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        return userRepository.save(user);
    }

    @Override
    public void changePassword(Long id ,UserPasswordRequest req) {
        log.info("Changing password for user {}", req);
        UserEntity user = getUserEntity(id);
//
//        // 1. Kiểm tra mật khẩu cũ có khớp không
//        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
//            throw new IllegalArgumentException("Mật khẩu cũ không đúng!");
//        }
//
//        // 2. Kiểm tra mật khẩu mới và confirm có giống nhau không
//        if (!req.getPassword().equals(req.getConfirmPassword())) {
//            throw new IllegalArgumentException("Mật khẩu mới không khớp xác nhận!");
//        }
//
//        // 3. Cập nhật mật khẩu
//        user.setPassword(passwordEncoder.encode(req.getPassword()));
//        userRepository.save(user);
//
//        log.info("Changed password successfully for user id: {}", id);

        if (req.getPassword().equals(req.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        userRepository.save(user);
        log.info("Changed password for user {}", user);
    }

    @Override
    public void blockOrEnable(Long id) {
        UserEntity user = getUserEntity(id);
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    private UserEntity getUserEntity(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
