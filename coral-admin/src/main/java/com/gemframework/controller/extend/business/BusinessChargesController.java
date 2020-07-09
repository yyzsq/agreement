
package com.gemframework.controller.extend.business;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gemframework.annotation.Log;
import com.gemframework.common.utils.GemBeanUtils;
import com.gemframework.constant.GemModules;
import com.gemframework.controller.prekit.BaseController;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.common.PageInfo;
import com.gemframework.model.common.validator.SaveValidator;
import com.gemframework.model.common.validator.UpdateValidator;
import com.gemframework.model.entity.po.*;
import com.gemframework.model.entity.vo.*;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.gemframework.common.constant.GemSessionKeys.CURRENT_USER_KEY;

/**
 * @Title: BusinessChargesController
 * @Date: 2020-05-20 17:44:45
 * @Version: v1.0
 * @Description: 业务收费表控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/business/businessCharges")
public class BusinessChargesController extends BaseController {

    private static final String moduleName = "业务收费表";

    @Autowired
    private BusinessChargesService businessChargesService;
    @Autowired
    private AssociatedServicesService associatedServicesService;
    @Autowired
    private ServiceSettingsService serviceSettingsService;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerResearchService customerResearchService;
    @Autowired
    private FeeReceivableService feeReceivableService;
    @Autowired
    private ContractInfoService contractInfoService;
    @Autowired
    private TichengContractService tichengContractService;
    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("businessCharges:page")
    public BaseResultData page(PageInfo pageInfo, BusinessChargesVo vo) {
        String tichengId=null;
        if(StringUtils.isNotBlank(vo.getTichengId())){
            tichengId=vo.getTichengId();
            vo.setTichengId(null);
        }

        String userId=null;
        if(StringUtils.isNotBlank(vo.getUserId())){
            userId=vo.getUserId();
            vo.setUserId(null);
        }
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        queryWrapper.orderByDesc("create_time");
        if(userId!=null) {
            queryWrapper.eq("user_id", userId);
        }
        if(tichengId!=null){
            List<String> tcList=new ArrayList();
            QueryWrapper wrapper6=new QueryWrapper();
            wrapper6.eq("ti_cheng_id",tichengId);
            wrapper6.eq("type","1");
            List<TichengContract> list=tichengContractService.list(wrapper6);
            for(TichengContract contract:list){
                tcList.add(contract.getGuanLianId());
            }
            queryWrapper.in("id",tcList);
        }
        Page page = businessChargesService.page(setOrderPage(pageInfo),queryWrapper);
        List list=new ArrayList();
        for(Object obj:page.getRecords()){
            BusinessCharges info=(BusinessCharges)obj;
            BusinessChargesVo entity = GemBeanUtils.copyProperties(info, BusinessChargesVo.class);
            AssociatedServicesVo servicesVo=new AssociatedServicesVo();
            servicesVo.setType("1");
            servicesVo.setContractId(info.getId()+"");
            if(info.getCustomerId()!=null) {
                CustomerResearch research = customerResearchService.getById(info.getCustomerId());
                entity.setCustomerName(research.getCorporateName());
            }
            QueryWrapper wrapper = makeQueryMaps(servicesVo);
            List<AssociatedServices> list1=associatedServicesService.list(wrapper);
            entity.setServicesList(list1);
            User user=userService.getById(entity.getUserId());
            entity.setUserName(user.getRealname());
            if(entity.getClerksId()!=null){
                User user1=userService.getById(entity.getClerksId());
                entity.setClerksName(user1.getRealname());
            }
            String[] colors={"layui-badge-pink","layui-badge-green","layui-badge-blue","layui-badge-yellow"};
            int ix=0;
            String serviceInfo="";
            for(AssociatedServices services:list1) {
                ServiceSettings settings = serviceSettingsService.getById(services.getServiceId());
                if(settings==null){
                    continue;
                }
                serviceInfo += "<span class='layui-badge " + colors[ix] + "'>" + settings.getServiceName() + "</span>";
                if (ix == 3) {
                    ix = 0;
                } else {
                    ix++;
                }
            }
            entity.setServices(serviceInfo);
            list.add(entity);
        }


        return BaseResultData.SUCCESS(list,page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("businessCharges:list")
    public BaseResultData list(BusinessChargesVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        queryWrapper.orderByDesc("create_time");
        List list = businessChargesService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("businessCharges:save")
    public BaseResultData save(@RequestBody BusinessChargesVo vo) {
        User userInfo=(User) SecurityUtils.getSubject().getSession().getAttribute(CURRENT_USER_KEY);
        vo.setUserId(userInfo.getId()+"");
        vo.setStatus("0");
        boolean ifalg=true;
        ContractInfoVo contractInfoVo=vo.getContractInfoVo();
        String services=null;
        if(vo.getServices()!=null&&vo.getServices()!=""){
            services=vo.getServices();
            vo.setServices(null);
        }
        CustomerResearchVo researchVo=new CustomerResearchVo();
        researchVo.setCorporateName(vo.getCustomerName());
        QueryWrapper queryWrapper = makeQueryMaps(researchVo);

        List<CustomerResearch> cuList = customerResearchService.list(queryWrapper);
        if(cuList.size()>0){
            ifalg=false;
            vo.setCustomerId(cuList.get(0).getId()+"");
            //contractInfoVo.setCustomerName(vo.getCustomerName());
        }else{
            CustomerResearchVo researchVo1 =new CustomerResearchVo();
            researchVo1.setCorporateName(vo.getCustomerName());
            GemValidate(researchVo1, SaveValidator.class);
            CustomerResearch research = GemBeanUtils.copyProperties(researchVo1, CustomerResearch.class);
            if(!customerResearchService.save(research)){
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        }

        vo.setCreateTime(new Date());
        GemValidate(vo, SaveValidator.class);
        BusinessCharges entity = GemBeanUtils.copyProperties(vo, BusinessCharges.class);
        if(!businessChargesService.save(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        if(vo.getContractInfoVo()!=null){
            contractInfoVo.setServiceId(entity.getId()+"");
            GemValidate(contractInfoVo, SaveValidator.class);
            ContractInfo entity3 = GemBeanUtils.copyProperties(contractInfoVo, ContractInfo.class);
            if(!contractInfoService.save(entity3)){
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }


        }
        if(services!=null) {
            JSONArray array = JSONArray.parseArray(services);
            for (Object obj : array) {
                JSONObject json = (JSONObject) obj;
                AssociatedServices associatedServices = new AssociatedServices();
                String serviceId = json.getString("serviceId");
                String price = json.getString("price");
                String remark = json.getString("remark");
                associatedServices.setPrice(new BigDecimal(price));
                associatedServices.setRemark(remark);
                associatedServices.setServiceId(serviceId);
                associatedServices.setContractId(entity.getId() + "");
                associatedServices.setType("1");
                GemValidate(associatedServices, SaveValidator.class);
                if (!associatedServicesService.save(associatedServices)) {
                    return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
                }
            }
        }
        FeeReceivable receivable=new FeeReceivable();
        receivable.setCustomerId(entity.getCustomerId());
        receivable.setGuanLianId(entity.getServiceId());
        receivable.setMoney(entity.getMoney());
        receivable.setType("1");
        receivable.setStatus("0");
        receivable.setUserId(entity.getUserId());
        receivable.setCustomerName(entity.getCustomerName());
        receivable.setPayType(entity.getPayType());
        GemValidate(receivable, SaveValidator.class);
        if(!feeReceivableService.save(receivable)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        if(vo.getTkFlag()) {
            FeeReceivableVo receivable1 = new FeeReceivableVo();
            receivable1.setCustomerName(vo.getCustomerName());
            receivable1.setType("2");
            receivable1.setStatus("1");
            QueryWrapper queryWrapper1 = makeQueryMaps(receivable1);
            List<FeeReceivable> list3 = feeReceivableService.list(queryWrapper1);
            for (FeeReceivable vable : list3) {
                vable.setStatus("3");
                feeReceivableService.updateById(vable);
            }
        }
        return BaseResultData.SUCCESS(entity);
    }


    /**
     * 删除 & 批量刪除
     * @return
     */
    @Log(type = OperateType.ALTER,value = "删除"+moduleName)
    @PostMapping("/delete")
    @RequiresPermissions("businessCharges:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) {
            BusinessCharges charges=businessChargesService.getById(id);
            businessChargesService.removeById(id);
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("guan_lian_id",charges.getServiceId());
            List<FeeReceivable> list=feeReceivableService.list(wrapper);
            for(FeeReceivable receivable:list){
                feeReceivableService.removeById(receivable.getId());
            }
        }
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                for(Long reId:listIds){
                    BusinessCharges charges=businessChargesService.getById(reId);
                    QueryWrapper wrapper=new QueryWrapper();
                    wrapper.eq("guan_lian_id",charges.getServiceId());
                    List<FeeReceivable> list=feeReceivableService.list(wrapper);
                    for(FeeReceivable receivable:list){
                        feeReceivableService.removeById(receivable.getId());
                    }
                }
                    businessChargesService.removeByIds(listIds);
            }
        }
        return BaseResultData.SUCCESS();
    }

    @PostMapping("/paidan")
    @RequiresPermissions("businessCharges:paidan")
    public BaseResultData paidan(@RequestBody BusinessChargesVo vo) {

        GemValidate(vo, UpdateValidator.class);
        BusinessCharges entity = GemBeanUtils.copyProperties(vo, BusinessCharges.class);
        if(!businessChargesService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }


    /**
     * 编辑
     * @return
     */
    @Log(type = OperateType.ALTER,value = "编辑"+moduleName)
    @PostMapping("/update")
    @RequiresPermissions("businessCharges:update")
    public BaseResultData update(@RequestBody BusinessChargesVo vo) {
        vo.setStatus("0");
        BusinessCharges oldInfo=businessChargesService.getById(vo.getId());
        String services=null;
        if(vo.getServices()!=null&&vo.getServices()!=""){
            services=vo.getServices();
            vo.setServices(null);
        }
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("corporate_name",vo.getCustomerName());
        List<CustomerResearch> list1=customerResearchService.list(wrapper);
        if(list1.size()>0){
           vo.setCustomerId(list1.get(0).getId()+"");
        }
        GemValidate(vo, UpdateValidator.class);
        BusinessCharges entity = GemBeanUtils.copyProperties(vo, BusinessCharges.class);
        if(!businessChargesService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        //先删除
        AssociatedServicesVo servicesVo=new AssociatedServicesVo();
        servicesVo.setContractId(entity.getId()+"");
        servicesVo.setType("1");
        QueryWrapper queryWrapper3 = makeQueryMaps(servicesVo);
        List<AssociatedServices> assList=associatedServicesService.list(queryWrapper3);
        for(AssociatedServices services1:assList){
            associatedServicesService.removeById(services1.getId());
        }
        if(services!=null) {
            JSONArray array = JSONArray.parseArray(services);
            for (Object obj : array) {
                JSONObject json = (JSONObject) obj;
                AssociatedServices associatedServices = new AssociatedServices();
                String serviceId = json.getString("serviceId");
                String price = json.getString("price");
                String remark = json.getString("remark");
                associatedServices.setPrice(new BigDecimal(price));
                associatedServices.setRemark(remark);
                associatedServices.setServiceId(serviceId);
                associatedServices.setContractId(entity.getId() + "");
                associatedServices.setType("1");
                GemValidate(associatedServices, SaveValidator.class);
                if (!associatedServicesService.save(associatedServices)) {
                    return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
                }
            }
        }


        FeeReceivableVo feeReceivableVo=new FeeReceivableVo();
        feeReceivableVo.setGuanLianId(entity.getServiceId());
        QueryWrapper queryWrapper = makeQueryMaps(feeReceivableVo);
        queryWrapper.in("type","0","1");
        List<FeeReceivable> list=feeReceivableService.list(queryWrapper);
        if(list.size()>0) {
            FeeReceivable receivable = list.get(0);
            BigDecimal total=receivable.getMoney().subtract(oldInfo.getMoney());
            total=entity.getMoney().add(total);
            receivable.setMoney(total);
            receivable.setCustomerName(entity.getCustomerName());
            receivable.setCustomerId(entity.getCustomerId());
            receivable.setUserId(entity.getUserId());
            receivable.setPayType(entity.getPayType());
            receivable.setStatus("0");
            if (!feeReceivableService.updateById(receivable)) {
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        }
        return BaseResultData.SUCCESS(entity);
    }


    /**
     * 获取用户信息ById
     * @return
     */
    @Log(type = OperateType.NORMAL,value = "查看"+moduleName)
    @GetMapping("/info")
    @RequiresPermissions("businessCharges:info")
    public BaseResultData info(Long id) {
        BusinessCharges info = businessChargesService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

    @GetMapping("/getServiceId")
    public BaseResultData getServiceId(){
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String date = f.format(new Date());
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("deleted","0");
        queryWrapper.like("service_id","Y"+date);
        queryWrapper.orderByDesc("service_id");
        List<BusinessCharges> list=businessChargesService.list(queryWrapper);
        if(list.size()==0){
            date="Y"+date+"0001";
        }else{
            String id=list.get(0).getServiceId();
            id=id.substring(9);
            Integer serviceId=Integer.parseInt(id);
            serviceId+=1;
            String sesId="";
            int len=serviceId.toString().length();
            if(len==1){
                sesId="000"+serviceId;
            }else if(len==2){
                sesId="00"+serviceId;
            }else if(len==3){
                sesId="0"+serviceId;
            }else{
                sesId=serviceId.toString();
            }
            date="Y"+date+sesId;
        }
        return BaseResultData.SUCCESS(date);
    }
}