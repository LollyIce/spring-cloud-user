package com.user.service;

import com.user.entity.UserInfo;

public interface UserService {

    UserInfo findByOpenid(String openid);
}
