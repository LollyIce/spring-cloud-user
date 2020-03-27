package com.user.VO;

import lombok.Data;

import java.util.Date;
@Data
public class UserEvaluateVO {

    private String username;

    private Date rateTime;

    //评论的星星数
    private Integer score;

    //满意度 0/满意 1/不满意
    private Integer rateType;

    //评论内容
    private String text;

    //头像
    private String avatar;
}
