package com.user.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


@Data
@ToString
@Entity
public class UserEvaluate {

    @Id
    private String id;

    //用户ID
    private String userId;

    //评论时间
    private Date rateTime;

    //评论的星星数
    private Integer score;

    //满意度 0/满意 1/不满意
    private Integer rateType;

    //评论内容
    private String text;
}
