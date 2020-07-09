
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.CustomerResearchMapper;
import com.gemframework.model.entity.po.CustomerResearch;
import com.gemframework.service.CustomerResearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Title: CustomerResearchServiceImpl
 * @Date: 2020-05-10 17:39:43
 * @Version: v1.0
 * @Description: 客户信息表服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class CustomerResearchServiceImpl extends ServiceImpl<CustomerResearchMapper, CustomerResearch> implements CustomerResearchService {

    @Autowired
    private CustomerResearchMapper customerResearchMapper;
    public CustomerResearch getInfoByName(String name){
      return  customerResearchMapper.getInfoByName(name);
    }
}