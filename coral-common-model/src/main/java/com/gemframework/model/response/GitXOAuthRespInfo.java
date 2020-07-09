package com.gemframework.model.response;

import lombok.Data;

@Data
public class GitXOAuthRespInfo {
    //第三方返回数据
    private Integer id;
    //昵称
    private String name;
    //头像地址
    private String avatarUrl;
    //登录帐号
    private String login;
    //个人介绍
    private String bio;
    //个人博客
    private String blog;
    //主页地址
    private String htmlUrl;
    //创建时间
    private String createdAt;
    //更新时间
    private String updatedAt;
}