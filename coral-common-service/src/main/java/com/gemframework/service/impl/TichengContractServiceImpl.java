
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.TichengContractMapper;
import com.gemframework.model.entity.po.TichengContract;
import com.gemframework.service.TichengContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Title: TichengContractServiceImpl
 * @Date: 2020-06-22 11:29:17
 * @Version: v1.0
 * @Description: 提成关联合同编号服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class TichengContractServiceImpl extends ServiceImpl<TichengContractMapper, TichengContract> implements TichengContractService {

}