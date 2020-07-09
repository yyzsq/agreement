
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.MonthlyReportMapper;
import com.gemframework.model.entity.po.ContractInfo;
import com.gemframework.model.entity.po.MonthlyReport;
import com.gemframework.service.ContractInfoService;
import com.gemframework.service.MonthlyReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * @Title: MonthlyReportServiceImpl
 * @Date: 2020-05-15 09:24:01
 * @Version: v1.0
 * @Description: 月报表服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class MonthlyReportServiceImpl extends ServiceImpl<MonthlyReportMapper, MonthlyReport> implements MonthlyReportService {
    @Autowired
    private ContractInfoService contractInfoService;

   // @Scheduled(cron = "0 0 4 * * ? *")
   @Scheduled(cron = "0 0 4 * * ?")
    public void saveMonthlyReport(){
        QueryWrapper wrapper=new QueryWrapper();
        List stsIn=new ArrayList();
        stsIn.add("0");
        stsIn.add("1");
        stsIn.add("2");
        stsIn.add("3");
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-01 00:00:00");

        wrapper.gt("service_end_time",sf.format(new Date()));
        wrapper.in("status",stsIn);
        List<ContractInfo> list=contractInfoService.list(wrapper);
        for(ContractInfo info:list){
            if(info.getAccounting()==null){
                continue;
            }
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("customer_name",info.getCustomerName());
            queryWrapper.gt("create_time",sf.format(new Date()));
            int count=this.count(queryWrapper);
            if(count==0){
                MonthlyReport monthlyReport=new MonthlyReport();
                monthlyReport.setCustomerName(info.getCustomerName());
                if(info.getCustomerId()!=null) {
                    monthlyReport.setCustomerId(info.getCustomerId() + "");
                }
                    monthlyReport.setStatus("0");
                    monthlyReport.setCreateTime(new Date());
                    monthlyReport.setUserId(info.getAccounting());
                    this.save(monthlyReport);
            }
        }
    }
}