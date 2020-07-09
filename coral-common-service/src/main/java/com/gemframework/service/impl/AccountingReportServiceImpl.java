
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.AccountingReportMapper;
import com.gemframework.model.entity.po.AccountingReport;
import com.gemframework.service.AccountingReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Title: AccountingReportServiceImpl
 * @Date: 2020-05-13 09:51:44
 * @Version: v1.0
 * @Description: 会计工作报告服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class AccountingReportServiceImpl extends ServiceImpl<AccountingReportMapper, AccountingReport> implements AccountingReportService {

}