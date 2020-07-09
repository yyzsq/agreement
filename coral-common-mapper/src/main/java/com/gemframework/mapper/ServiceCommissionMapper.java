
package com.gemframework.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gemframework.model.entity.po.ServiceCommission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Title: ServiceCommissionMapper
 * @Date: 2020-05-28 11:46:31
 * @Version: v1.0
 * @Description: 业务提成持久层
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Repository
public interface ServiceCommissionMapper extends BaseMapper<ServiceCommission> {

    public List<Map> getCommissioniInfo(Map map);
}