
package com.gemframework.model.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemframework.model.common.BaseEntityVo;
import com.gemframework.model.common.validator.SuperValidator;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class MemberVo extends BaseEntityVo {

    @NotBlank(message = "账号不能为空",groups = SuperValidator.class)
    private String account;
    private String password;
    private String salt;
    private String avatarUrl;
    private String nickname;
    private String phone;
    private String email;
    private String wechat;
    private String blog;
    //用户状态 0 正常 1禁用
    private Integer status;
    //注册渠道 1 手动添加 2 自助注册 3 oauth
    private Integer channel;
    //是否第三方登录 0 否 1是
    private Integer isOauth;
    //第三方平台 1 gitee 2github 3 微信 4 QQ 5 微博 6支付宝
    private Integer thirdParty;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;
}