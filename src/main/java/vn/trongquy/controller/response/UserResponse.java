package vn.trongquy.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.trongquy.common.Gender;
import vn.trongquy.common.UserStatus;
import vn.trongquy.model.UserEntity;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
public class UserResponse{

    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String userName;

    private Gender gender;

    private Date birthday;

    private String email;

    private String phone;

    private UserStatus status;
//
//    private String facebookAccountId;
//
//    private String googleAccountId;

    public static UserResponse fromUser(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .phone(user.getPhone())
                .email(user.getEmail())
                .status(UserStatus.ACTIVE)
                .build();
    }
}
