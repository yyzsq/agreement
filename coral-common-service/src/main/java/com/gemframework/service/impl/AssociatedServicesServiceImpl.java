
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.AssociatedServicesMapper;
import com.gemframework.model.entity.po.AssociatedServices;
import com.gemframework.service.AssociatedServicesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Title: AssociatedServicesServiceImpl
 * @Date: 2020-05-19 09:07:48
 * @Version: v1.0
 * @Description: 关联服务服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class AssociatedServicesServiceImpl extends ServiceImpl<AssociatedServicesMapper, AssociatedServices> implements AssociatedServicesService {

}