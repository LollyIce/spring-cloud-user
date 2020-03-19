package com.user.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "seller_info")
public class UserInfo {

    @Id
    @Column(name = "seller_id")
    private String id;

    private String username;

    private String password;

    private String openid;

    private Integer role;
}
