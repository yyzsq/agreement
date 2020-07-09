
package com.gemframework.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.ContractInfoMapper;
import com.gemframework.model.entity.po.ContractInfo;
import com.gemframework.model.entity.po.CustomerResearch;
import com.gemframework.model.entity.po.WechatNotice;
import com.gemframework.service.ContractInfoService;
import com.gemframework.service.CustomerResearchService;
import com.gemframework.service.WechatNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Title: ContractInfoServiceImpl
 * @Date: 2020-05-10 18:23:44
 * @Version: v1.0
 * @Description: 合同信息表服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class ContractInfoServiceImpl extends ServiceImpl<ContractInfoMapper, ContractInfo> implements ContractInfoService {



    @Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "0 */1 * * * ?")
    public void testAsyncJob() {
        QueryWrapper query=new QueryWrapper();
        query.in("status","0","1","2");
        List<ContractInfo> list=this.list(query);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String newDate=format.format(new Date());
        for(ContractInfo info:list){
            if(info.getStatus().equals("0")||info.getStatus().equals("1")){
                    ContractInfo obj=new ContractInfo();
                    obj.setStatus("2");
                    obj.setId(info.getId());
                    this.updateById(obj);
            }
                Calendar calendar = Calendar.getInstance();//日历度对象
                calendar.setTime(info.getServiceEndTime());//设置当前日知期道
                Calendar cal=Calendar.getInstance();
                if(cal.getTime().compareTo(calendar.getTime())==1){
                    ContractInfo obj=new ContractInfo();
                    obj.setStatus("3");
                    obj.setId(info.getId());
                    this.updateById(obj);
                }
        }
    }


    @Override
    public boolean updateById(CustomerResearch entity) {
        return false;
    }
}