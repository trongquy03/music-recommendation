package vn.trongquy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import vn.trongquy.model.UserEntity;
import vn.trongquy.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Slf4j(topic = "USER-CONTROLLER")
@Tag(name = "UserController")
public class UserController {
    private final UserService userService;

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
    public ResponseEntity<ResponseObject> updateUser( @PathVariable Long userId,
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

}
