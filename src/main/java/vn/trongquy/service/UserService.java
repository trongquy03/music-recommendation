package vn.trongquy.service;

import vn.trongquy.controller.request.UserCreationRequest;
import vn.trongquy.controller.request.UserPasswordRequest;
import vn.trongquy.controller.response.UserResponse;
import vn.trongquy.model.UserEntity;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse findByUsername(String username);

    UserResponse findByEmail(String email);

    UserEntity save(UserCreationRequest req);

    void update(UserCreationRequest req);

    void changePassword(UserPasswordRequest req);

    void delete(Long id);
}
