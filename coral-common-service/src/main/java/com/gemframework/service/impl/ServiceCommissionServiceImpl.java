
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.ServiceCommissionMapper;
import com.gemframework.model.entity.po.*;
import com.gemframework.model.entity.vo.AssociatedServicesVo;
import com.gemframework.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Title: ServiceCommissionServiceImpl
 * @Date: 2020-05-28 11:46:31
 * @Version: v1.0
 * @Description: 业务提成服务实现类
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class ServiceCommissionServiceImpl extends ServiceImpl<ServiceCommissionMapper, ServiceCommission> implements ServiceCommissionService {

    @Autowired
    private ServiceCommissionMapper serviceCommissionMapper;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;
    @Autowired
    private BusinessChargesService businessChargesService;
    @Autowired
    private AssociatedServicesService associatedServicesService;
    @Autowired
    private BusinessSettingsService businessSettingsService;
    @Autowired
    private ContractInfoService contractInfoService;
    @Autowired
    private ServiceSettingsService serviceSettingsService;
    @Autowired
    private TichengContractService tichengContractService;

    @Scheduled(cron = "0 0 5 ? * MON")
    //@Scheduled(cron = "0 */1 * * * ?")
    public void testAsyncJob() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7); //得到前一天道
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startDate=df.format(date)+" 00:00:00";
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(calendar2.DATE, -1);
        Date endDate = calendar2.getTime();
        String endDate2=df.format(endDate)+" 23:59:59";
        QueryWrapper wrapper=new QueryWrapper();

        List<BusinessSettings> buList=businessSettingsService.list();
        Map<String,BusinessSettings> map=new HashMap();
        for(BusinessSettings set:buList){
            map.put(set.getId()+"",set);
        }

        List<User> list= userService.list(wrapper);
        for(User user:list){
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.between("create_time",startDate,endDate2);
            queryWrapper.eq("user_id",user.getId());
            //获得业务人员
            int countYw=businessChargesService.count(queryWrapper);
            if(countYw>0){
                for(BusinessSettings settings:buList) {
                    List<String> contracts=new ArrayList();
                    Map infoMap=new HashMap();
                    infoMap.put("userId",user.getId());
                    infoMap.put("serviceId",settings.getServiceId());
                    List<Map> mapList=serviceCommissionMapper.getCommissioniInfo(infoMap);
                    BigDecimal decimal=new BigDecimal(0);
                    if(mapList.size()==0){
                        continue;
                    }
                    int total=0;
                    for(Map map1:mapList){
                        decimal=decimal.add((BigDecimal)map1.get("price"));
                        total+=1;
                        contracts.add(map1.get("id").toString());
                    }
                    ServiceCommission serviceCommission=new ServiceCommission();
                    serviceCommission.setCountMoney(decimal);
                    serviceCommission.setType("0");
                    serviceCommission.setProportion(settings.getCommission());
                    serviceCommission.setDate(startDate.substring(0,10)+"-"+endDate2.substring(0,10));
                    serviceCommission.setOther1(settings.getBusinessName());
                    serviceCommission.setUserId(user.getId()+"");
                    serviceCommission.setDanNumber(total+"");
                    BigDecimal ticheng=getTicheng(settings.getCommission(),decimal);
                    if(ticheng.intValue()==0){
                        continue;
                    }
                    serviceCommission.setMoney(ticheng);
                    this.save(serviceCommission);
                    TichengContract tichengContract=new TichengContract();
                    for(String contractId:contracts){
                        tichengContract.setGuanLianId(contractId);
                        tichengContract.setType("1");
                        tichengContract.setTiChengId(serviceCommission.getId()+"");
                        tichengContractService.save(tichengContract);
                    }
                }
            }

            QueryWrapper queryWrapper2=new QueryWrapper();
            queryWrapper2.eq("deleted",0);
            queryWrapper2.between("create_time",startDate,endDate2);
            queryWrapper2.eq("clerks_id",user.getId());
            //获得办事人员
            int countbs=businessChargesService.count(queryWrapper2);
            //获得办事提成
            if(countbs>0){
                List<String> contracts=new ArrayList();
                for(BusinessSettings settings:buList) {
                    Map infoMap=new HashMap();
                    infoMap.put("clerksId",user.getId());
                    infoMap.put("serviceId",settings.getServiceId());
                    List<Map> mapList=serviceCommissionMapper.getCommissioniInfo(infoMap);
                    BigDecimal decimal=new BigDecimal(0);
                    if(mapList.size()==0){
                        continue;
                    }
                    int total=0;
                    for(Map map1:mapList){
                        total+=1;
                        decimal=decimal.add((BigDecimal)map1.get("price"));
                        contracts.add(map1.get("id").toString());
                    }
                    ServiceCommission serviceCommission=new ServiceCommission();
                    serviceCommission.setCountMoney(decimal);
                    serviceCommission.setType("1");
                    serviceCommission.setProportion(settings.getCommission());
                    serviceCommission.setDate(startDate.substring(0,10)+"-"+endDate2.substring(0,10));
                    serviceCommission.setOther1(settings.getBusinessName());
                    serviceCommission.setUserId(user.getId()+"");
                    BigDecimal ticheng=getTicheng(settings.getCommission(),decimal);
                    if(ticheng.intValue()==0){
                        continue;
                    }
                    serviceCommission.setDanNumber(total+"");
                    serviceCommission.setMoney(ticheng);
                    this.save(serviceCommission);
                    TichengContract tichengContract=new TichengContract();
                    for(String contractId:contracts){
                        tichengContract.setGuanLianId(contractId);
                        tichengContract.setType("1");
                        tichengContract.setTiChengId(serviceCommission.getId()+"");
                        tichengContractService.save(tichengContract);
                    }
                }
            }
            //合同新户签订人员
            QueryWrapper queryWrapper3=new QueryWrapper();
            queryWrapper3.eq("deleted",0);
            queryWrapper3.between("sign_time",startDate,endDate2);
            queryWrapper3.eq("signed_by",user.getId());

            int qdrCounr=contractInfoService.count(queryWrapper3);
            if(qdrCounr>0){
                List<ContractInfo> conList=contractInfoService.list(queryWrapper3);
                List<String> contracts=new ArrayList<>();
                ServiceSettings serviceSettings=null;
                for(ContractInfo info:conList){
                    QueryWrapper queryWrapper4 = new QueryWrapper();
                    queryWrapper4.eq("type","0");
                    queryWrapper4.eq("contract_id",info.getId());
                    List<AssociatedServices> assList=associatedServicesService.list(queryWrapper4);
                    for(AssociatedServices services:assList){
                        ServiceSettings settings=serviceSettingsService.getById(services.getServiceId());
                        if(settings.getServiceName().equals("新户标识")){
                            serviceSettings=settings;
                            contracts.add(info.getId()+"");
                        }
                    }
                }
                if(contracts.size()>0&&serviceSettings!=null) {
                    QueryWrapper wrapper1=new QueryWrapper();
                    wrapper1.eq("service_id",serviceSettings.getId());
                    wrapper1.eq("type","0");
                    List<BusinessSettings> list1=businessSettingsService.list(wrapper1);
                    BusinessSettings settings=list1.get(0);
                    ServiceCommission serviceCommission = new ServiceCommission();
                    serviceCommission.setCountMoney(new BigDecimal(0));
                    serviceCommission.setType("2");
                    serviceCommission.setProportion(settings.getCommission());
                    serviceCommission.setDate(startDate.substring(0, 10) + "-" + endDate2.substring(0, 10));
                    serviceCommission.setOther1(settings.getBusinessName());
                    serviceCommission.setUserId(user.getId() + "");
                    BigDecimal ticheng = getTicheng(settings.getCommission(), new BigDecimal(0));
                    if (ticheng.intValue() == 0) {
                        continue;
                    }
                    serviceCommission.setDanNumber(contracts.size() + "");
                    serviceCommission.setMoney(ticheng);
                    this.save(serviceCommission);
                    TichengContract tichengContract=new TichengContract();
                    for(String contractId:contracts){
                        tichengContract.setGuanLianId(contractId);
                        tichengContract.setType("1");
                        tichengContract.setTiChengId(serviceCommission.getId()+"");
                        tichengContractService.save(tichengContract);
                    }
                }
            }

        }


    }

    public BigDecimal getTicheng(String tic,BigDecimal money){
        NumberFormat nf= NumberFormat.getPercentInstance();
        Number m=null;
        BigDecimal result=new BigDecimal(0);
        try {

            int inx = tic.indexOf("%");
            if (inx != -1) {
                m = nf.parse(tic);
                BigDecimal big = new BigDecimal(m.doubleValue());
                big = big.setScale(4, RoundingMode.HALF_UP);
                result = money.multiply(big);
                result = result.setScale(4, RoundingMode.HALF_UP);
            } else {
                BigDecimal big = new BigDecimal(tic);
                result = big.setScale(4, RoundingMode.HALF_UP);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Map> getCommissioniInfo(Map map){
        return serviceCommissionMapper.getCommissioniInfo(map);
    }
}