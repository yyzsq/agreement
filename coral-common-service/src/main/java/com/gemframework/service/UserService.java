
package com.gemframework.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.User;
import com.gemframework.model.entity.vo.UserVo;

import java.util.List;

public interface UserService extends IService<User> {

    UserVo save(UserVo vo);

    UserVo update(UserVo vo);

    //根据用户名获取用户信息
    List<UserVo> pageByParams(Page page, Wrapper wrapper);



    //根据用户名获取用户信息
    User getByUserName(String username);

    User getById(Long id);

    boolean exits(User entity);
}