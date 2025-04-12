package vn.trongquy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.trongquy.common.UserStatus;
import vn.trongquy.controller.request.UserCreationRequest;
import vn.trongquy.controller.request.UserPasswordRequest;
import vn.trongquy.controller.request.UserUpdateRequest;
import vn.trongquy.controller.response.UserPageResponse;
import vn.trongquy.controller.response.UserResponse;
import vn.trongquy.exception.ResourceNotFoundException;
import vn.trongquy.model.UserEntity;
import vn.trongquy.repository.UserRepository;
import vn.trongquy.service.UserService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserPageResponse findAll(String keyword, String sort, int page, int size) {
        if (StringUtils.hasLength(keyword)) {

        }

        //sorting
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                String column = matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("asc")) {
                    order = new Sort.Order(Sort.Direction.ASC, column);
                }else {
                    order = new Sort.Order(Sort.Direction.DESC, column);
                }
            }
        }

        // page start = 1
        int pageNo = 0;
        if (page > 0){
            pageNo = page - 1;
        }

        //paging
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(order));

        Page<UserEntity> userEntities = userRepository.findAll(pageable);

        List<UserResponse> userList = userEntities.stream().map(entity -> UserResponse.builder()
                .id(entity.getId())
                .userName(entity.getUsername())
                .gender(entity.getGender())
                .birthday(entity.getBirthday())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .status(entity.getStatus())
                .build()

        ).toList();

        UserPageResponse response = new UserPageResponse();
        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalElements(userEntities.getTotalElements());
        response.setTotalPages(userEntities.getTotalPages());
        response.setUsers(userList);

        return response;
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
