
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.CollectionInfoMapper;
import com.gemframework.model.entity.po.CollectionInfo;
import com.gemframework.model.entity.po.ContractInfo;
import com.gemframework.model.entity.po.CustomerResearch;
import com.gemframework.model.entity.po.FeeReceivable;
import com.gemframework.model.entity.vo.CollectionInfoVo;
import com.gemframework.service.CollectionInfoService;
import com.gemframework.service.ContractInfoService;
import com.gemframework.service.FeeReceivableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @Title: CollectionInfoServiceImpl
 * @Date: 2020-05-11 14:19:02
 * @Version: v1.0
 * @Description: 催费报表服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class CollectionInfoServiceImpl extends ServiceImpl<CollectionInfoMapper, CollectionInfo> implements CollectionInfoService {

    @Autowired
    private ContractInfoService contractInfoService;
    @Autowired
    private CustomerResearchServiceImpl customerResearchService;
    @Autowired
    private FeeReceivableService feeReceivableService;

    @Override
    public boolean saveColle(ContractInfo co) {
        CollectionInfo collectionInfo=new CollectionInfo();
        collectionInfo.setUserId(co.getAccounting());
        collectionInfo.setFirmId(co.getCustomerId()+"");

        return false;
    }

    public void saveCollectionInfo(CollectionInfoVo vo) throws ParseException {

        QueryWrapper wrapper=new QueryWrapper();
        wrapper.gt("weishou_money",0);
        List<ContractInfo> contractInfos=contractInfoService.list(wrapper);
        for(ContractInfo info:contractInfos){
            CollectionInfo collectionInfo=new CollectionInfo();
            collectionInfo.setPrice(info.getWeishouMoney());
            if(info.getCustomerName()!=null){
                collectionInfo.setOther1(info.getCustomerName());
                if(info.getCustomerId()!=null){
                    collectionInfo.setFirmId(info.getCustomerId()+"");
                }
            }else{
                QueryWrapper query=new QueryWrapper();
                query.eq("customer_id",info.getCustomerId());
                CustomerResearch research=customerResearchService.getOne(query);
                collectionInfo.setOther1(research.getCorporateName());
                collectionInfo.setFirmId(info.getCustomerId()+"");
            }
            collectionInfo.setUserId(info.getAccounting());
            collectionInfo.setExpirationDate(info.getServiceEndTime());
            collectionInfo.setOther2("0");
            collectionInfo.setCollectionId(info.getContractId());
            collectionInfo.setNextTime(new Date());

            QueryWrapper wrapper1=new QueryWrapper();
            wrapper1.like("guan_lian_id",info.getContractId());
            wrapper1.in("type","0","1");
            wrapper1.orderByDesc("create_time");
            List<FeeReceivable> list=feeReceivableService.list(wrapper1);
            FeeReceivable feeReceivable=list.get(0);
            collectionInfo.setUpTime(feeReceivable.getCreateTime());
            collectionInfo.setStatus("0");
            save(collectionInfo);
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        List list=new ArrayList();
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        queryWrapper.in("status",list);
        Calendar cal_1=Calendar.getInstance();//获取当前日期
        cal_1.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(vo.getStartDate()));
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,日期既为本月第一天
        Calendar cal_2=Calendar.getInstance();//获取当前日期
        cal_2.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(vo.getEndDate()));
        cal_2.add(Calendar.MONTH, +1);
        cal_2.set(Calendar.DAY_OF_MONTH,1);
        cal_2.add(Calendar.DAY_OF_MONTH,-1);//设置为1号,日期既为月份最后一天

        queryWrapper.between("service_time",new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal_1.getTime()),new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(cal_2.getTime()));
        List<ContractInfo> list1=contractInfoService.list(queryWrapper);

        for(ContractInfo info:list1){
            QueryWrapper queryWrapper1=new QueryWrapper();
            queryWrapper1.eq("customer_name",info.getCustomerName());
            List slist=new ArrayList();
            slist.add("0");
            slist.add("1");
            slist.add("2");
            queryWrapper1.orderByDesc("service_end_time");
            queryWrapper1.in("status",slist);
            List<ContractInfo> list2=contractInfoService.list(queryWrapper1);
            if(list2.size()==0){
                continue;
            }
            ContractInfo contractInfo1=list2.get(0);
            if(contractInfo1.getServiceEndTime().compareTo(cal_2.getTime())==1){
                continue;
            }
            CollectionInfo info2=new CollectionInfo();
            if(contractInfo1.getAccounting()!=null)
                info2.setUserId(contractInfo1.getAccounting());
            if(contractInfo1.getContractId()!=null)
                info2.setCollectionId(contractInfo1.getContractId());
            if(contractInfo1.getCustomerName()!=null)
                info2.setOther1(contractInfo1.getCustomerName());
            if(contractInfo1.getAmountMoney()!=null)
                info2.setPrice(contractInfo1.getAmountMoney());
            if(contractInfo1.getCustomerId()!=null)
                info2.setFirmId(contractInfo1.getCustomerId()+"");

            info2.setUpTime(contractInfo1.getSignTime());
            info2.setNextTime(contractInfo1.getServiceEndTime());
            info2.setExpirationDate(contractInfo1.getServiceEndTime());
            info2.setOther2("1");
            save(info2);
        }

    }
}