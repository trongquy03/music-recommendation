package vn.trongquy.controller.request;

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
    private String phone;
    private UserType type;

}