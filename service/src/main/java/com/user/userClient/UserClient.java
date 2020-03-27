package com.user.userClient;

import com.user.entity.UserEvaluate;
import com.user.entity.UserInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user")
public interface UserClient {
    @PostMapping("/user/getUserByIds")
    public List<UserInfo> getUserByIds(@RequestBody List<String> ids);

}
