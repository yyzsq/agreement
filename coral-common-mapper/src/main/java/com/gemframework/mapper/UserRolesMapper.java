
package com.gemframework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gemframework.model.entity.po.UserRoles;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Title: UserRolesMapper
 * @Package: com.gemframework.mapper
 * @Date: 2020-03-09 15:31:33
 * @Version: v1.0
 * @Description: Mapper接口
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@Repository
public interface UserRolesMapper extends BaseMapper<UserRoles> {

    List<Long> findIdsByUserId(Long userId);
}