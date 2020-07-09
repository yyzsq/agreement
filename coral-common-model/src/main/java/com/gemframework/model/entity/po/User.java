
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

import java.util.Date;


@TableName("gem_sys_user")
@Data
public class User extends BaseEntityPo {

    private Long deptId;
    private String username;
    private String password;
    private String salt;
    private String avatarUrl;
    private String realname;
    private Integer jobnumber;
    private String post;
    private Integer sex;
    private Date birthday;
    private Integer province;
    private Integer city;
    private Integer area;
    private String address;
    private String phone;
    private String tel;
    private String email;
    private String qq;
    //用户状态 0 正常 1禁用
    private Integer status;
    private String level;
}