
package com.gemframework.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.RoleMapper;
import com.gemframework.model.entity.po.Role;
import com.gemframework.model.entity.po.User;
import com.gemframework.model.entity.po.UserRoles;
import com.gemframework.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Autowired
    private UserRolesService userRolesService;

    @Autowired
    private RoleDeptsService roleDeptsService;

    @Autowired
    private RoleRightsService roleRightsService;

    @Transactional
    public boolean delete(Long id, String ids) {
        if(id!=null){
            //刪除角色，顺便删除角色部门关联关系、角色权限关联关系
            if(removeById(id)){
                roleDeptsService.deleteByRoleId(id);
                roleRightsService.deleteByRoleId(id);
            }
        }

        //批量删除角色，遍历删除角色部门关联关系、角色权限关联关系
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                if(removeByIds(listIds)){
                    for(Long roleId:listIds){
                        roleDeptsService.deleteByRoleId(roleId);
                        roleRightsService.deleteByRoleId(roleId);
                    }
                }
            }
        }
        return true;
    }


    @Override
    public Set<String> findRolesFlagByUsername(String username) {
        //用户角色标识列表
        Set<String> rolesSet = new HashSet<>();
        User user = userService.getByUserName(username);
        QueryWrapper<UserRoles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        List<UserRoles> userRoles = userRolesService.list(queryWrapper);
        if(userRoles != null && !userRoles.isEmpty()){
            for(UserRoles userRole:userRoles){
                Role role = this.getById(userRole.getRoleId());
                if(role!=null && StringUtils.isNotBlank(role.getFlag())){
                    rolesSet.add(role.getFlag());
                }
            }
        }
        return rolesSet;
    }

    @Override
    public Set<Role> findRolesByFlags(Set<String> flags) {
        //用户角色列表
        Set<Role> roles = new HashSet<>();
        if(flags!=null && !flags.isEmpty()){
            for(String flag:flags){
                QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("flag",flag);
                Role role = this.getOne(queryWrapper);
                if(role!=null){
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    @Override
    public Set<Role> findRolesByUsername(String username) {
        //用户角色列表
        Set<Role> roles = new HashSet<>();
        User user = userService.getByUserName(username);
        QueryWrapper<UserRoles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        List<UserRoles> userRoles = userRolesService.list(queryWrapper);
        if(userRoles != null && !userRoles.isEmpty()){
            for(UserRoles userRole:userRoles){
                Role role = this.getById(userRole.getRoleId());
                if(role!=null){
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    @Override
    public boolean exits(Role entity) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        queryWrapper.and(wrapper -> wrapper.eq("name",entity.getName()).or().eq("flag",entity.getFlag()));
        //编辑
        if(entity.getId() != null && entity.getId() !=0){
            queryWrapper.ne("id",entity.getId());
        }
        log.info("queryWrapper="+ JSON.toJSONString(queryWrapper));
        if(count(queryWrapper)>0){
            return true;
        }
        return false;
    }

}