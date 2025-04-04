package vn.trongquy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.trongquy.controller.request.UserCreationRequest;
import vn.trongquy.controller.response.ResponseObject;
import vn.trongquy.controller.response.UserResponse;
import vn.trongquy.model.UserEntity;
import vn.trongquy.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "UserController")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user list", description = "Api retrieve from db")
    @GetMapping("")
    public List<UserResponse> getList(
            @RequestParam(required = false) String keyWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return null;
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

}
