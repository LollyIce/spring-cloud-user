package com.user.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginUser {

    private String openid;

    private String password;
}
