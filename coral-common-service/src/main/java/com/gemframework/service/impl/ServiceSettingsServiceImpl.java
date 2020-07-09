
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.ServiceSettingsMapper;
import com.gemframework.model.entity.po.ServiceSettings;
import com.gemframework.service.ServiceSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Title: ServiceSettingsServiceImpl
 * @Date: 2020-05-11 11:14:55
 * @Version: v1.0
 * @Description: 合同信息表服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class ServiceSettingsServiceImpl extends ServiceImpl<ServiceSettingsMapper, ServiceSettings> implements ServiceSettingsService {

}