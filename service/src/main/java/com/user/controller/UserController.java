package com.user.controller;

import com.user.VO.ResultVO;
import com.user.VO.UserEvaluateVO;
import com.user.entity.UserEvaluate;
import com.user.entity.UserInfo;
import com.user.repository.UserEvaluateRepository;
import com.user.service.UserService;
import com.user.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserEvaluateRepository evaluateRepository;

    /***
     * 根据用户ID 查询用户
     * @param ids
     * @return
     */
    @PostMapping("/getUserByIds")
    public List<UserInfo> getUserByIds(@RequestBody List<String> ids){
        return userService.findUserByIds(ids);
    }

    /****
     *  获取评论数据
     * @return
     */
    @GetMapping("/ratings")
    @CrossOrigin
    public ResultVO ratings(){
        //获取所有评论
        List<UserEvaluate> all = evaluateRepository.findAll();
        //用户去重ID
        List<String> userIds = all.stream().map(UserEvaluate::getUserId).distinct().collect(Collectors.toList());
        //获取评论用户
        List<UserInfo> users = userService.findUserByIds(userIds);
        //拼装数据
        List<UserEvaluateVO> vos = new ArrayList<>();
        for (UserEvaluate userEvaluate : all) {
            UserEvaluateVO vo = new UserEvaluateVO();
            BeanUtils.copyProperties(userEvaluate,vo);
            for (UserInfo user:users) {
                if(userEvaluate.getUserId().equals(user.getId())){
                    vo.setUsername(user.getUsername());
                    vo.setAvatar(user.getAvatar());
                }
            }
            vos.add(vo);
        }
        return ResultVOUtil.success(vos);
    }

}
