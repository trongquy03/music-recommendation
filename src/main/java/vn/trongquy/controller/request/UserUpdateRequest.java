package vn.trongquy.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.ToString;
import vn.trongquy.common.Gender;

import java.io.Serializable;
import java.util.Date;

@Getter
@ToString
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "username must be not blank")
    private String username;
    private Gender gender;
    private Date birthday;
    private String email;

    @Pattern(
            regexp = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$",
            message = "Invalid phone number format"
    )
    private String phone;

}
