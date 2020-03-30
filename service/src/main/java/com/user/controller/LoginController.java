package com.user.controller;

import com.user.Enums.ResultEnum;
import com.user.Enums.RoleEnum;
import com.user.SMS.SmsBase;
import com.user.VO.ResultVO;
import com.user.constant.CookieConstant;
import com.user.constant.RedisConstant;
import com.user.entity.UserInfo;
import com.user.service.UserService;
import com.user.utils.CodeUtil;
import com.user.utils.CookieUtil;
import com.user.utils.ResultVOUtil;
import com.user.utils.UUIDGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.jni.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /***
     * 买家登录
     * @param openid
     * @param response
     * @return
     */
    @GetMapping("/buyer")
    public ResultVO buyer(@RequestParam("openid") String openid, HttpServletResponse response){
        //1.openid和数据库里的数据是否匹配
        UserInfo byOpenid = userService.findByOpenid(openid);
        if(byOpenid == null){
            return ResultVOUtil.error(ResultEnum.LOGIN_FAIL);
        }
        //2.判断角色
        if(byOpenid.getRole() != RoleEnum.BUYER.getCode()){
            return ResultVOUtil.error(ResultEnum.ROLE_ERROR);
        }
        //3.cookie里设置openid=abc
        CookieUtil.set(response, CookieConstant.OPENID,openid,CookieConstant.expire);
        return ResultVOUtil.success();
    }

    /***
     * 手机登录 发送验证码
     * @param phoneNums
     * @return
     */
    @GetMapping("/buyerPhoneSMS")
    public ResultVO buyerPhoneSMS(@RequestParam("phoneNums") String phoneNums){
        //生成随机六位验证码
        String code = CodeUtil.getNonce_str();
        //发送验证码短信
        SmsBase.mobileQuery(phoneNums,code);
        //将验证码存入Redis  key:phoneNums value:code
        stringRedisTemplate.opsForValue().set(phoneNums,code,60,TimeUnit.SECONDS);
        return ResultVOUtil.success();
    }

    /***
     * 手机登录 校验验证码
     * @param phoneNums
     * @return
     */
    @GetMapping("/buyerPhoneLogin")
    public ResultVO buyerPhoneLogin(@RequestParam("phoneNums") String phoneNums,
                                    @RequestParam("code") String code,
                                    HttpServletResponse response){
        String redisCode = stringRedisTemplate.opsForValue().get(phoneNums);
        if(!code.equals(redisCode)){
            return ResultVOUtil.error(ResultEnum.CODE_ERROR);
        }
        //登录成功 将用户名 存入redis 并将key存入cookie
        String uuid = UUIDGenerator.generate();
        stringRedisTemplate.opsForValue().set(uuid,phoneNums,2,TimeUnit.HOURS);
        CookieUtil.set(response,CookieConstant.OPENID,uuid,CookieConstant.expire);
        return ResultVOUtil.success();
    }

    /***
     * 校验是否已登录
     * @param request
     * @return
     */
    @GetMapping("/checkLogin")
    public ResultVO checkLogin(HttpServletRequest request){
        Cookie cookie = CookieUtil.get(request, CookieConstant.OPENID);
        if(StringUtils.isEmpty(cookie.getValue())){
            if(StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(cookie.getValue()))){
                return ResultVOUtil.success();
            }
        }
        return ResultVOUtil.error(ResultEnum.NOT_LOGIN);
    }

    /***
     * 卖家登录
     * @param openid
     * @param response
     * @return
     */
    @GetMapping("/seller")
    public ResultVO seller(@RequestParam("openid") String openid,
                           HttpServletResponse response,
                           HttpServletRequest request){

        //判断是否已登录
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie != null && StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(String.format(CookieConstant.TOKEN,cookie.getValue())))){
            return ResultVOUtil.success();
        }
        //1.openid和数据库里的数据是否匹配
        UserInfo byOpenid = userService.findByOpenid(openid);
        if(byOpenid == null){
            return ResultVOUtil.error(ResultEnum.LOGIN_FAIL);
        }
        //2.判断角色
        if(byOpenid.getRole() != RoleEnum.SELLER.getCode()){
            return ResultVOUtil.error(ResultEnum.ROLE_ERROR);
        }
        //3.redis设置key = UUID,value = xyz
        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_TEMPLATE,token),
                openid,
                CookieConstant.expire,  //设置过期时间
                TimeUnit.SECONDS);      //单位为s
        //4.cookie里设置openid=abc
        CookieUtil.set(response, CookieConstant.TOKEN,token,CookieConstant.expire);
        return ResultVOUtil.success();
    }
}
