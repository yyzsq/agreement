
package com.gemframework.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gemframework.model.entity.po.CustomerResearch;
import org.springframework.stereotype.Repository;

/**
 * @Title: CustomerResearchMapper
 * @Date: 2020-05-10 17:39:43
 * @Version: v1.0
 * @Description: 客户信息表持久层
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Repository
public interface CustomerResearchMapper extends BaseMapper<CustomerResearch> {

    public CustomerResearch getInfoByName(String name);

}