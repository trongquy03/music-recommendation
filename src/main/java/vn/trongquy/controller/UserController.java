package vn.trongquy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.trongquy.controller.request.UserCreationRequest;
import vn.trongquy.controller.request.UserPasswordRequest;
import vn.trongquy.controller.request.UserUpdateRequest;
import vn.trongquy.controller.response.ResponseObject;
import vn.trongquy.controller.response.UserPageResponse;
import vn.trongquy.controller.response.UserResponse;
import vn.trongquy.model.EmailVerification;
import vn.trongquy.model.UserEntity;
import vn.trongquy.repository.EmailVerificationRepository;
import vn.trongquy.repository.UserRepository;
import vn.trongquy.service.EmailService;
import vn.trongquy.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Slf4j(topic = "USER-CONTROLLER")
@Tag(name = "UserController")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    @Operation(summary = "Get user list", description = "Api retrieve from db")
    @GetMapping("")
    public ResponseEntity<ResponseObject> getList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UserPageResponse pageResponse = userService.findAll(keyword,sort , page, size );
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK)
                .data(pageResponse)
                .message("Get user list")
                .build());

    }

    @Operation(summary = "Create user", description = "Api retrieve from db")
    @PostMapping("/register")
    public ResponseEntity<ResponseObject> createUser(@Valid @RequestBody UserCreationRequest request,
                                                     BindingResult result) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .message(errorMessages.toString())
                    .build());
        }

        UserEntity user =userService.save(request);
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.CREATED)
                .data(UserResponse.fromUser(user))
                .message("Account registration successful")
                .build());
    }

    @Operation(summary = "Update user", description = "Api retrieve from db")
    @PutMapping("/update/{userId}")
    public ResponseEntity<ResponseObject> updateUser(@Valid @PathVariable Long userId,
                                                      @RequestBody UserUpdateRequest request,
                                                     BindingResult result) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .message(errorMessages.toString())
                    .build());
        }

        UserEntity user = userService.update(userId,request);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Update user detail successfully")
                        .data(UserResponse.fromUser(user))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @Operation(summary = "Get user detail", description = "Api retrieve from db")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseObject> getUserDetail(@PathVariable @Min(value = 1, message = "UserId must be equals or greater than 1") Long userId) {
        UserEntity user = userService.findById(userId);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .data(UserResponse.fromUser(user))
                        .build()
        );
    }


    @Operation(summary = "Update user", description = "Api retrieve from db")
    @PutMapping("/change-pwd/{userId}")
    public ResponseEntity<ResponseObject> changePassword(@Valid @PathVariable long userId,@RequestBody UserPasswordRequest request){

        userService.changePassword(userId, request);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Change pw user successfully")
                        .status(HttpStatus.OK)
                        .build());
    }

    @Operation(summary = "Delete user", description = "Api retrieve from db")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResponseObject> blockOrEnable(@PathVariable @Min(value = 1, message = "UserId must be equals or greater than 1") Long userId){
        userService.blockOrEnable(userId);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Delete user successfully")
                        .build()
        );
    }

    @Operation(summary = "Confirm Email", description = "Confirm email for account")
    @GetMapping("/confirm-email")
    public void confirmEmail(@RequestParam String secretCode, HttpServletResponse response) throws IOException {
        log.info("Confirm email for account with secretCode: {}", secretCode);

        try {
            // Kiểm tra mã xác minh trong cơ sở dữ liệu
            Optional<EmailVerification> emailVerificationOptional = emailVerificationRepository.findByVerificationCode(secretCode);

            if (emailVerificationOptional.isPresent()) {
                EmailVerification emailVerification = emailVerificationOptional.get();


                // Cập nhật trạng thái xác minh thành công
                UserEntity user = emailVerification.getUser();
                user.setEmailVerified(true);
                userRepository.save(user);

                // Xóa mã xác minh đã sử dụng
                emailVerificationRepository.delete(emailVerification);

                // Chuyển hướng người dùng đến trang thành công
                log.info("Email verified successfully for user: {}", user.getEmail());
                response.sendRedirect("https://tayjava.vn/verification-success");
            } else {
                log.error("Verification code not found");
                response.sendRedirect("https://tayjava.vn/verification-failed?reason=notfound");
            }
        } catch (Exception e) {
            log.error("Verification failed", e.getMessage(), e);
            response.sendRedirect("https://tayjava.vn/verification-failed?reason=error");
        } finally {
            // Đảm bảo rằng luôn chuyển hướng đến trang wp-admin
            log.info("Email verification process completed, redirecting...");
            response.sendRedirect("https://tayjava.vn/wp-admin/");
        }
    }

}
