package vn.trongquy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.trongquy.common.UserStatus;
import vn.trongquy.controller.request.UserCreationRequest;
import vn.trongquy.controller.request.UserPasswordRequest;
import vn.trongquy.controller.response.UserResponse;
import vn.trongquy.model.UserEntity;
import vn.trongquy.repository.UserRepository;
import vn.trongquy.service.UserService;

import java.util.List;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserResponse> findAll() {
        return List.of();
    }

    @Override
    public UserResponse findById(Long id) {
        return null;
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
    public void update(UserCreationRequest req) {

    }

    @Override
    public void changePassword(UserPasswordRequest req) {

    }

    @Override
    public void delete(Long id) {

    }
}
