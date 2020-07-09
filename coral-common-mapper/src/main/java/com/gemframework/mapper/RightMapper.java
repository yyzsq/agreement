
package com.gemframework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gemframework.model.entity.po.Right;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Title: RightMapper
 * @Package: com.gemframework.mapper
 * @Date: 2020-03-09 15:31:33
 * @Version: v1.0
 * @Description: Mapper接口
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@Repository
public interface RightMapper extends BaseMapper<Right> {

    List<Right> findRightsByRoleAndType(Map<Object, Object> map);
}