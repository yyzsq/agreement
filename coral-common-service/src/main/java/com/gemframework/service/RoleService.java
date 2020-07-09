
package com.gemframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.Role;

import java.util.Set;

public interface RoleService extends IService<Role> {

    boolean delete(Long id, String ids);

    Set<String> findRolesFlagByUsername(String username);

    Set<Role> findRolesByFlags(Set<String> flags);

    Set<Role> findRolesByUsername(String username);

    boolean exits(Role entity);
}