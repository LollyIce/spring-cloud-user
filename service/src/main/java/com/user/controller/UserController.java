package com.user.controller;

import com.user.entity.UserInfo;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    /***
     * 根据用户ID 查询用户
     * @param ids
     * @return
     */
    @PostMapping("/getUserByIds")
    public List<UserInfo> getUserByIds(@RequestBody List<String> ids){
        return userService.findUserByIds(ids);
    }
}
