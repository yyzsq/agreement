
package com.gemframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.Right;
import com.gemframework.model.entity.po.Role;
import com.gemframework.model.enums.MenuType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RightService extends IService<Right> {

    Set<String> findRightsByRoles(Set<Role> roles);

    List<Right> findRightsByRolesAndType(Set<String> roles, MenuType type);

    boolean exits(Right entity);
}