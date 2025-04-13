package vn.trongquy.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class UserPasswordRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "oldPassword must be not blank")
    private String oldPassword;

    @NotBlank(message = "password must be not blank")
    private String password;

    @NotBlank(message = "confirmPassword must be not blank")
    private String confirmPassword;
}
