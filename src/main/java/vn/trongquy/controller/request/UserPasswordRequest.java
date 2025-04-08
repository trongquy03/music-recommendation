package vn.trongquy.controller.request;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class UserPasswordRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String oldPassword;
    private String password;
    private String confirmPassword;
}
