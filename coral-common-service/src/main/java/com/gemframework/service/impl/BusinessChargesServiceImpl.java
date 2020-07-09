
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.BusinessChargesMapper;
import com.gemframework.model.entity.po.BusinessCharges;
import com.gemframework.service.BusinessChargesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @Title: BusinessChargesServiceImpl
 * @Date: 2020-05-20 17:44:45
 * @Version: v1.0
 * @Description: 业务收费表服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class BusinessChargesServiceImpl extends ServiceImpl<BusinessChargesMapper, BusinessCharges> implements BusinessChargesService {


    /*@Scheduled(cron="0 0 0 0 0 1 ")
    public void testAsyncJob() {

    }*/
}