
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.HandoverRecordMapper;
import com.gemframework.model.entity.po.HandoverRecord;
import com.gemframework.service.HandoverRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Title: HandoverRecordServiceImpl
 * @Date: 2020-05-15 10:31:46
 * @Version: v1.0
 * @Description: 交接记录服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class HandoverRecordServiceImpl extends ServiceImpl<HandoverRecordMapper, HandoverRecord> implements HandoverRecordService {

}