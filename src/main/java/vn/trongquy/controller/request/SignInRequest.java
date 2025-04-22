package vn.trongquy.controller.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SignInRequest implements Serializable {
    private String userName;
    private String password;
}
