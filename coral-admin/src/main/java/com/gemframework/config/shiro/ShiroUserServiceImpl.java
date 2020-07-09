
package com.gemframework.config.shiro;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.common.exception.GemException;
import com.gemframework.common.utils.GemBeanUtils;
import com.gemframework.mapper.UserMapper;
import com.gemframework.model.entity.po.Role;
import com.gemframework.model.entity.po.User;
import com.gemframework.model.entity.po.UserRoles;
import com.gemframework.model.entity.vo.RoleVo;
import com.gemframework.model.entity.vo.UserRolesVo;
import com.gemframework.model.entity.vo.UserVo;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.service.RoleService;
import com.gemframework.service.UserRolesService;
import com.gemframework.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("shiroUserServiceImpl")
public class ShiroUserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRolesService userRolesService;

    @Override
    public UserVo save(UserVo vo) {
        //1、 saveOrUpdate
        User user = GemBeanUtils.copyProperties(vo,User.class);
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(32);
        user.setSalt(salt);
        user.setPassword(ShiroUtils.passwordSHA256(user.getPassword(), user.getSalt()));
        if(exits(user)){
            throw new GemException(ErrorCode.USER_EXIST);
        }
        if(super.saveOrUpdate(user)){
            vo.setId(user.getId());
            if(StringUtils.isNotBlank(vo.getRoleIds())){
                //2、 保存用户关联角色信息
                UserRolesVo userRolesVo = new UserRolesVo();
                userRolesVo.setUserId(vo.getId());
                userRolesVo.setRoleIds(vo.getRoleIds());
                userRolesService.save(userRolesVo);
            }
        }
        return vo;
    }

    @Override
    public UserVo update(UserVo vo) {
        //1、 saveOrUpdate
        User user = GemBeanUtils.copyProperties(vo,User.class);
        if(StringUtils.isNotBlank(user.getPassword())){
            //sha256加密
            String salt = RandomStringUtils.randomAlphanumeric(32);
            user.setSalt(salt);
            user.setPassword(ShiroUtils.passwordSHA256(user.getPassword(), user.getSalt()));
        }
        if(super.updateById(user)){
            vo.setId(user.getId());
            //2、 保存用户关联角色信息
            if(StringUtils.isNotBlank(vo.getRoleIds())){
                UserRolesVo userRolesVo = new UserRolesVo();
                userRolesVo.setUserId(vo.getId());
                userRolesVo.setRoleIds(vo.getRoleIds());
                userRolesService.save(userRolesVo);
            }
        }
        return vo;
    }

    @Override
    public List<UserVo> pageByParams(Page page, Wrapper wrapper) {
        page = super.page(page,wrapper);
        List<User> users = page.getRecords();
        List<UserVo> userVos = GemBeanUtils.copyCollections(users,UserVo.class);
        for(UserVo userVo:userVos){
            //查询userRoles
            List<RoleVo> roles = new ArrayList<>();
            List<UserRoles> userRolesList = userRolesService.findByUserId(userVo.getId());
            for(UserRoles userRoles:userRolesList){
                Role role = roleService.getById(userRoles.getRoleId());
                roles.add(GemBeanUtils.copyProperties(role,RoleVo.class));
            }
            userVo.setRoles(roles);
            //回显时密码不显示
            userVo.setPassword("");
        }
        return userVos;
    }

    @Override
    public User getByUserName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public User getById(Long id) {
        return super.getById(id);
    }

    @Override
    public boolean exits(User entity) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",entity.getUsername())
                .or()
                .eq("phone",entity.getPhone())
                .or()
                .eq("email",entity.getEmail());
        //编辑
        if(entity.getId() != null && entity.getId() !=0){
            queryWrapper.and(wrapper -> wrapper.ne("id",entity.getId()));
        }
        if(count(queryWrapper)>0){
            return true;
        }
        return false;
    }
}