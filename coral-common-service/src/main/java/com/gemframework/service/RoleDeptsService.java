
package com.gemframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.RoleDepts;
import com.gemframework.model.entity.vo.RoleDeptsVo;

public interface RoleDeptsService extends IService<RoleDepts> {

    /**
     * 保存角色部门表
     * @param vo
     * @return
     */
    boolean save(RoleDeptsVo vo);

    /**
     * 通过角色ID删除
     * @param roleId
     * @return
     */
    boolean deleteByRoleId(Long roleId);

}