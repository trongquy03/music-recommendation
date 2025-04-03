package vn.trongquy.controller.request;

import java.io.Serializable;

public class UserPasswordRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String password;
    private String confirmPassword;
}
