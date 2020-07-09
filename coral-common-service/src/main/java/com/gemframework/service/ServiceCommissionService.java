
package com.gemframework.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.ServiceCommission;

import java.util.List;
import java.util.Map;

/**
 * @Title: ServiceCommissionService
 * @Date: 2020-05-28 11:46:31
 * @Version: v1.0
 * @Description: 业务提成服务接口
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
public interface ServiceCommissionService extends IService<ServiceCommission> {
    public List<Map> getCommissioniInfo(Map map);

    public void testAsyncJob();
}