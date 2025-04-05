package vn.trongquy.controller.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.ToString;
import vn.trongquy.common.Gender;
import vn.trongquy.common.UserType;


import java.io.Serializable;
import java.util.Date;

@Getter
@ToString
public class UserCreationRequest implements Serializable {
    private String username;
    private Gender gender;
    private Date birthday;
    private String email;

    @Pattern(
            regexp = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$",
            message = "Invalid phone number format"
    )
    private String phone;
    private UserType type;

}