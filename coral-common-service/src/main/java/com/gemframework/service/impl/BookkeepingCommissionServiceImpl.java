
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.BookkeepingCommissionMapper;
import com.gemframework.model.entity.po.*;
import com.gemframework.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Title: BookkeepingCommissionServiceImpl
 * @Date: 2020-05-15 16:19:14
 * @Version: v1.0
 * @Description:
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class BookkeepingCommissionServiceImpl extends ServiceImpl<BookkeepingCommissionMapper, BookkeepingCommission> implements BookkeepingCommissionService {

    @Autowired
    @Qualifier("shiroUserServiceImpl")
    private UserService userService;
    @Autowired
    private ContractInfoService contractInfoService;
    @Autowired
    private BookingSettingsService bookingSettingsService;
    @Autowired
    private AssociatedServicesService associatedServicesService;
    @Autowired
    private ServiceSettingsService serviceSettingsService;
    @Autowired
    private TichengContractService tichengContractService;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


    //记账提成
    @Scheduled(cron = "0 0 3 1 * ?")
    //@Scheduled(cron = "0 */1 * * * ?")
    public void testAsyncJob() {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("deleted",0);
        List<User> list= userService.list(wrapper);

        QueryWrapper wrapper4=new QueryWrapper();
        wrapper4.eq("other1","-1");
        List<BookingSettings> bookList=bookingSettingsService.list(wrapper4);
        Map<String,BookingSettings> map=new HashMap();
        for(BookingSettings set:bookList){
            map.put(set.getId()+"",set);
        }
        QueryWrapper wrapper5=new QueryWrapper();
        List list3=new ArrayList();
        Map<String,BookingSettings> map2=new HashMap();
        list3.add("0");
        list3.add("1");
        list3.add("2");
        list3.add("3");
        list3.add("4");
        wrapper5.in("other1",list3);
        List<BookingSettings> bookList2=bookingSettingsService.list(wrapper5);
        for(BookingSettings set:bookList2){
            map2.put(set.getOther1()+"_"+set.getOther2(),set);
        }
        for(User user:list){
            QueryWrapper query=new QueryWrapper();
            Calendar   cal_1=Calendar.getInstance();//获取当前日期
            cal_1.add(Calendar.MONTH, -1);
            cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,日期既为本月第一天
            String firstDay = format.format(cal_1.getTime());
            query.eq("deleted",0);
            query.gt("service_time",firstDay+"");
            query.eq("accounting",user.getId());
            query.in("status","0","1","2");
            List<ContractInfo> conlist= contractInfoService.list(query);
            if(conlist.size()==0){
                continue;
            }

            //总记账合同金额
            BigDecimal decimal=new BigDecimal(0);
            BigDecimal xgmDec=new BigDecimal(0);
            BigDecimal ybnsrDec=new BigDecimal(0);
            BigDecimal gthDec=new BigDecimal(0);
            BigDecimal gthybnsr=new BigDecimal(0);
            int count=0;
            int gthCount=0;
            int xgmCount=0;
            int gtnsr=0;
            int xgmnsr=0;
            List<String> xgmList=new ArrayList();
            List<String> ybrList=new ArrayList();
            List<String> gthList=new ArrayList();
            List<String> gthybrList=new ArrayList();
            for(ContractInfo info:conlist){
                QueryWrapper wrapper1=new QueryWrapper();
                wrapper1.eq("contract_id",info.getId());
                List<AssociatedServices> servicesList=associatedServicesService.list(wrapper1);
                if(servicesList.size()==0){
                    continue;
                }
                BigDecimal totalPrice=new BigDecimal(0);
                for(AssociatedServices services:servicesList){
                    ServiceSettings settings=serviceSettingsService.getById(services.getServiceId());
                    if(settings.getServiceName().equals("代理记账")) {
                        totalPrice = totalPrice.add(services.getPrice());
                    }
                }
                if(totalPrice.intValue()==0){
                    continue;
                }
                if(info.getServiceMonthly()==null) {
                    info.setServiceMonthly("12");
                }

                if(info.getChargingStandard().equals("1")){
                    totalPrice=totalPrice.divide(new BigDecimal(3), 2, BigDecimal.ROUND_HALF_UP);
                }else  if(info.getChargingStandard().equals("2")){
                    totalPrice=totalPrice.divide(new BigDecimal(6), 2, BigDecimal.ROUND_HALF_UP);
                }else  if(info.getChargingStandard().equals("3")){
                    totalPrice=totalPrice.divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP);
                }

                /*if(Integer.parseInt(info.getServiceMonthly())>=12){
                    totalPrice=totalPrice.divide(new BigDecimal(12),2, BigDecimal.ROUND_HALF_UP);
                }*/

                decimal=decimal.add(info.getAmountMoney());

                if(info.getContractNature().equals("0")){
                    xgmList.add(info.getId()+"");
                    xgmDec=xgmDec.add(totalPrice);
                    xgmCount+=1;
                }else if(info.getContractNature().equals("1")){
                    ybrList.add(info.getId()+"");
                    ybnsrDec=ybnsrDec.add(totalPrice);
                    xgmnsr+=1;
                }else if(info.getContractNature().equals("2")){
                    gthList.add(info.getId()+"");
                    gthDec=gthDec.add(totalPrice);
                    gthCount+=1;
                }else if(info.getContractNature().equals("3")){
                    gthybrList.add(info.getId()+"");
                    gthybnsr=gthybnsr.add(totalPrice);
                    gtnsr+=1;
                }

            }

            if(xgmDec.compareTo(BigDecimal.ZERO)==0&&ybnsrDec.compareTo(BigDecimal.ZERO)==0&&
                    gthDec.compareTo(BigDecimal.ZERO)==0&&gthybnsr.compareTo(BigDecimal.ZERO)==0){
                continue;
            }

            BookingSettings settings=new BookingSettings();
            BookingSettings book=map.get(user.getLevel());
            int xingji=0;
            if(book.getLevenName().equals("一星")){
                xingji=0;
            }else if(book.getLevenName().equals("二星")){
                xingji=1;
            }else if(book.getLevenName().equals("三星")){
                xingji=2;
            }else if(book.getLevenName().equals("四星")){
                xingji=3;
            } else if(book.getLevenName().equals("五星")){
                xingji=4;
            }

            settings.setOther1(book.getLevenName());

            if(xgmDec.compareTo(BigDecimal.ZERO)!=0){
                BookingSettings settings1=map2.get(xingji+"_0");
                BookkeepingCommission commission=new BookkeepingCommission();
                commission.setAgencyNumber(xgmCount);
                commission.setGeneralTaxNumber(0);
                commission.setUserId(user.getId()+"");
                commission.setLevenName(settings1.getLevenName());
                commission.setPercentage(settings1.getCommission());
                commission.setCountMoney(decimal);
                commission.setOther1(new SimpleDateFormat("MM月").format(cal_1.getTime()));
                BigDecimal total=jisuanbil(settings1,xgmDec);
                if(total.compareTo(BigDecimal.ZERO)!=0){
                    commission.setMoney(total);
                    if(this.save(commission)){
                        TichengContract tichengContract=new TichengContract();
                        for(String contractId:xgmList){
                            tichengContract.setGuanLianId(contractId);
                            tichengContract.setType("0");
                            tichengContract.setTiChengId(commission.getId()+"");
                            tichengContractService.save(tichengContract);
                        }
                    }
                }
            }

            if(ybnsrDec.compareTo(BigDecimal.ZERO)!=0){
                BookingSettings settings1=map2.get(xingji+"_1");
                BookkeepingCommission commission=new BookkeepingCommission();
                commission.setAgencyNumber(xgmnsr);
                commission.setGeneralTaxNumber(1);
                commission.setUserId(user.getId()+"");
                commission.setLevenName(settings1.getLevenName());
                commission.setPercentage(settings1.getCommission());
                commission.setCountMoney(decimal);
                commission.setOther1(new SimpleDateFormat("MM月").format(cal_1.getTime()));
                BigDecimal total=jisuanbil(settings1,ybnsrDec);
                if(total.compareTo(BigDecimal.ZERO)!=0){
                    commission.setMoney(total);
                    if(this.save(commission)){
                        TichengContract tichengContract=new TichengContract();
                        for(String contractId:ybrList){
                            tichengContract.setGuanLianId(contractId);
                            tichengContract.setType("0");
                            tichengContract.setTiChengId(commission.getId()+"");
                            tichengContractService.save(tichengContract);
                        }
                    }
                }
            }

            if(gthDec.compareTo(BigDecimal.ZERO)!=0){
                BookingSettings settings1=map2.get(xingji+"_1");
                BookkeepingCommission commission=new BookkeepingCommission();
                commission.setAgencyNumber(gthCount);
                commission.setGeneralTaxNumber(2);
                commission.setUserId(user.getId()+"");
                commission.setLevenName(settings1.getLevenName());
                commission.setPercentage(settings1.getCommission());
                commission.setCountMoney(decimal);
                commission.setOther1(new SimpleDateFormat("MM月").format(cal_1.getTime()));
                BigDecimal total=jisuanbil(settings1,gthDec);
                if(total.compareTo(BigDecimal.ZERO)!=0){
                    commission.setMoney(total);
                    if(this.save(commission)) {
                        TichengContract tichengContract = new TichengContract();
                        for (String contractId : gthList) {
                            tichengContract.setGuanLianId(contractId);
                            tichengContract.setType("0");
                            tichengContract.setTiChengId(commission.getId() + "");
                            tichengContractService.save(tichengContract);
                        }
                    }
                }
            }

            if(gthybnsr.compareTo(BigDecimal.ZERO)!=0){
                BookingSettings settings1=map2.get(xingji+"_1");
                BookkeepingCommission commission=new BookkeepingCommission();
                commission.setAgencyNumber(gtnsr);
                commission.setGeneralTaxNumber(3);
                commission.setUserId(user.getId()+"");
                commission.setLevenName(settings1.getLevenName());
                commission.setPercentage(settings1.getCommission());
                commission.setCountMoney(decimal);
                commission.setOther1(new SimpleDateFormat("MM月").format(cal_1.getTime()));
                BigDecimal total=jisuanbil(settings1,gthybnsr);
                if(total.compareTo(BigDecimal.ZERO)!=0){
                    commission.setMoney(total);
                    if(this.save(commission)){
                        TichengContract tichengContract=new TichengContract();
                        for(String contractId:gthybrList){
                            tichengContract.setGuanLianId(contractId);
                            tichengContract.setType("0");
                            tichengContract.setTiChengId(commission.getId()+"");
                            tichengContractService.save(tichengContract);
                        }
                    }
                }
            }


        }
    }

    public static BigDecimal jisuanbil(BookingSettings book,BigDecimal decimal){
        NumberFormat nf= NumberFormat.getPercentInstance();
        Number m=null;
        try {
            int inx= book.getCommission().indexOf("%");
            if(inx!=-1) {
                m = nf.parse(book.getCommission());
                BigDecimal big=new BigDecimal(m.doubleValue());
                big = big.setScale(4, RoundingMode.HALF_UP);
                decimal= decimal.multiply(big);
                decimal = decimal.setScale(4, RoundingMode.HALF_UP);
            }else{
                BigDecimal big = new BigDecimal(book.getCommission());
                decimal = big.setScale(4, RoundingMode.HALF_UP);
            }
            return decimal;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  BigDecimal.ZERO;
    }
}