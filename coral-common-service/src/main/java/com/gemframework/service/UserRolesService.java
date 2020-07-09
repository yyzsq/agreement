
package com.gemframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.UserRoles;
import com.gemframework.model.entity.vo.UserRolesVo;

import java.util.List;

public interface UserRolesService extends IService<UserRoles> {

    /**
     * 通过用户ID查找关联关系
     * @param userId
     * @return
     */
    List<UserRoles> findByUserId(Long userId);

    /**
     * 通过用户ID查找关联关系ID
     * @param userId
     * @return
     */
    List<Long> findIdsByUserId(Long userId);


    /**
     * 通过用户ID删除
     * @param userId
     * @return
     */
    boolean deleteByUserId(Long userId);


    /**
     * 保存用户的角色关联关系
     * @return
     */
    boolean save(UserRolesVo vo);

}