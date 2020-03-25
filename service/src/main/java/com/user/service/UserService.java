package com.user.service;

import com.user.entity.UserInfo;

import java.util.List;

public interface UserService {

    UserInfo findByOpenid(String openid);

    List<UserInfo> findUserByIds(List<String> ids);
}
