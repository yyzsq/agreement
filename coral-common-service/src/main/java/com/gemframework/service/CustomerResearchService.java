
package com.gemframework.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.CustomerResearch;

/**
 * @Title: CustomerResearchService
 * @Date: 2020-05-10 17:39:43
 * @Version: v1.0
 * @Description: 客户信息表服务接口
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
public interface CustomerResearchService extends IService<CustomerResearch> {

    public CustomerResearch getInfoByName(String name);
}