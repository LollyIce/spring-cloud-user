package com.user.Enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    LOGIN_FAIL(1,"登录失败"),
    ROLE_ERROR(2,"角色权限有误"),
    PASSWORD_ERROR(3,"密码错误"),
    CODE_ERROR(4,"验证码错误")
    ;

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
