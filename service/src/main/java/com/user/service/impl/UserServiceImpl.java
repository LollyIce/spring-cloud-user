package com.user.service.impl;

import com.user.entity.UserInfo;
import com.user.repository.UserInfoRepository;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserInfo findByOpenid(String openid) {
        return userInfoRepository.findByOpenid(openid);
    }

    @Override
    public List<UserInfo> findUserByIds(List<String> ids) {
        return userInfoRepository.findByIdIn(ids);
    }
}
