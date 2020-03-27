package com.user.repository;

import com.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfo,String> {

    UserInfo findByOpenid(String openid);

    List<UserInfo> findByIdIn(List<String> ids);

}
