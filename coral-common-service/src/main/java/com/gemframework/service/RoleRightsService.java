
package com.gemframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.RoleRights;
import com.gemframework.model.entity.vo.RoleRightsVo;

public interface RoleRightsService extends IService<RoleRights> {

    /**
     * 保存角色功能权限
     * @param vo
     * @return
     */
    boolean save(RoleRightsVo vo);

    /**
     * 通过角色ID删除
     * @param roleId
     * @return
     */
    boolean deleteByRoleId(Long roleId);
}