
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.BusinessSettingsMapper;
import com.gemframework.model.entity.po.BusinessSettings;
import com.gemframework.service.BusinessSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Title: BusinessSettingsServiceImpl
 * @Date: 2020-05-11 12:25:05
 * @Version: v1.0
 * @Description: 业务提成设置服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class BusinessSettingsServiceImpl extends ServiceImpl<BusinessSettingsMapper, BusinessSettings> implements BusinessSettingsService {

}