
package com.gemframework.controller.extend.contract;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.common.utils.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
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
import com.gemframework.utils.DefaultInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import static com.gemframework.common.constant.GemSessionKeys.CURRENT_USER_KEY;

/**
 * @Title: ContractInfoController
 * @Date: 2020-05-10 18:23:44
 * @Version: v1.0
 * @Description: 合同信息表控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/contract/contractInfo")
public class ContractInfoController extends BaseController {

    private static final String moduleName = "合同信息表";

    @Autowired
    private ContractInfoService contractInfoService;

    @Autowired
    private UserRolesService userRolesService;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerResearchService customerResearchService;
    @Autowired
    private CollectionInfoService collectionInfoService;
    @Autowired
    private ServiceSettingsService serviceSettingsService;
    @Autowired
    private AssociatedServicesService associatedServicesService;
    @Autowired
    private FeeReceivableService feeReceivableService;
    @Autowired
    private BookingSettingsService bookingSettingsService;
    @Autowired
    private BusinessChargesService businessChargesService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TichengContractService tichengContractService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("contractInfo:page")
    public BaseResultData page(PageInfo pageInfo, ContractInfoVo vo) {
        String tichengId=vo.getTichengId();
        String tcType=vo.getTcType();
        vo.setTichengId(null);
        String status=vo.getStatus();
        vo.setStatus(null);
        User userInfo=(User)SecurityUtils.getSubject().getSession().getAttribute(CURRENT_USER_KEY);
        List<UserRoles> userRoles=userRolesService.findByUserId(userInfo.getId());
        String kjFlag="1";
        String jhFlag="1";
        for(UserRoles roles:userRoles){
            Role role=roleService.getById(roles.getRoleId());
            if(role.getFlag().equals("kuaiji")){

                kjFlag="2";
            }
            if(role.getFlag().equals("jihe")){
                jhFlag="2";
            }
            if(role.getFlag().equals("kefu")){
                jhFlag="2";
            }
            if(role.getFlag().equals("xz")){
                jhFlag="2";
            }
            if(role.getFlag().equals("admin")){
                jhFlag="2";
            }
        }
        String startDate=vo.getStartDate();
        vo.setStartDate(null);
        String endDate=vo.getEndDate();
        vo.setEndDate(null);
        String corporateName=null;
        if(vo.getCustomerName()!=null&&(!vo.getCustomerName().equals("undefined"))) {
             corporateName = vo.getCustomerName();
        }
        QueryWrapper<ContractInfo> queryWrapper = makeQueryMaps(vo);
        queryWrapper.orderByDesc("update_time");
        if(StringUtils.isNotBlank(tichengId)){
            List<String> tcList=new ArrayList();
            QueryWrapper wrapper6=new QueryWrapper();
            wrapper6.eq("ti_cheng_id",tichengId);
            wrapper6.eq("type",tcType);
            List<TichengContract> list=tichengContractService.list(wrapper6);
            for(TichengContract contract:list){
                tcList.add(contract.getGuanLianId());
            }
            queryWrapper.in("id",tcList);

        }
        if(jhFlag.equals("1")&&kjFlag.equals("2")){
            String userId=userInfo.getId().toString();
            queryWrapper.and(wrapper1 -> wrapper1.eq("signed_by",userId).or().eq("accounting",userId));
        }
        if(startDate!=null&&startDate.length()>0) {
            queryWrapper.between("sign_time", startDate, endDate);
        }
        queryWrapper.orderByDesc("id");
        String[] sts=status.split(",");
        queryWrapper.in("status",Arrays.asList(sts));
        Page page = contractInfoService.page(setOrderPage(pageInfo),queryWrapper);
        List list=new ArrayList();
        for(Object obj:page.getRecords()){
            ContractInfo info=(ContractInfo)obj;
            if(jhFlag.equals("1")&&kjFlag.equals("2")) {
                if (StringUtils.isNotBlank(info.getAccounting()) && !StringUtils.equals(info.getAccounting(), userInfo.getId().toString())) {
                    continue;
                }
            }
            ContractInfoVo entity = GemBeanUtils.copyProperties(info, ContractInfoVo.class);
            User user=userService.getById(entity.getSignedBy());
            User user1=userService.getById(entity.getAccounting());

            if(user!=null) {
                entity.setSignedName(user.getRealname());
            }
            if(user1!=null&&entity.getAccounting()!=null&&entity.getAccounting()!="") {
                entity.setAccountingName(user1.getRealname());
                if(user1.getLevel()!=null&&user1.getLevel().length()>0) {
                    BookingSettings settingsbooking = bookingSettingsService.getById(user1.getLevel());
                    entity.setLevelName(settingsbooking.getLevenName());
                }
            }

            QueryWrapper wrapper1=new QueryWrapper();
            wrapper1.eq("corporate_name",entity.getCustomerName());
            List<CustomerResearch> list3=customerResearchService.list(wrapper1);
            if(list3.size()!=0&&StringUtils.isNotBlank(list3.get(0).getPayTaxProperties())) {

                entity.setContractNature(list3.get(0).getPayTaxProperties());
            }
            AssociatedServicesVo servicesVo=new AssociatedServicesVo();
            servicesVo.setContractId(entity.getId()+"");
            servicesVo.setType("0");
            QueryWrapper wrapper = makeQueryMaps(servicesVo);
            List<AssociatedServices> list1=associatedServicesService.list(wrapper);
            entity.setServicesList(list1);
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
            if(corporateName!=null&&corporateName.length()>0){
                if(entity.getCustomerName().indexOf(corporateName)!=-1){
                    list.add(entity);
                }
            }else{
                list.add(entity);
            }

            }

            return BaseResultData.SUCCESS(list,page.getTotal());
    }

    @GetMapping("/listInfo")
    @RequiresPermissions("contractInfo:list")
    public BaseResultData listInfo(ContractInfoVo vo) {
        String status=vo.getStatus();
        vo.setStatus(null);
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        String[] sts=status.split(",");
        queryWrapper.in("status",Arrays.asList(sts));
        List<ContractInfo> list2 = contractInfoService.list(queryWrapper);
        Map map=new HashMap();
        map.put("list",list2);
        ContractInfo info= list2.get(0);
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("type","2");
        wrapper.eq("status","1");
        wrapper.eq("customer_name",info.getCustomerName());
        map.put("backFlag","false");
        List<FeeReceivable> list3=feeReceivableService.list(wrapper);
        BigDecimal totalMoney=new BigDecimal(0);
        if(list3.size()>0) {
            for (FeeReceivable vable : list3) {
                totalMoney = totalMoney.add(vable.getMoney());
            }
            map.put("backFlag","true");
            map.put("backMoney",totalMoney);
        }
        return BaseResultData.SUCCESS(map);
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("contractInfo:list")
    public BaseResultData list(ContractInfoVo vo) {
        String status=vo.getStatus();
        vo.setStatus(null);
        User userInfo=(User)SecurityUtils.getSubject().getSession().getAttribute(CURRENT_USER_KEY);
        List<UserRoles> userRoles=userRolesService.findByUserId(userInfo.getId());
        String kjFlag="1";
        String jhFlag="1";
        for(UserRoles roles:userRoles){
            Role role=roleService.getById(roles.getRoleId());
            if(role.getFlag().equals("kuaiji")){

                kjFlag="2";
            }
            if(role.getFlag().equals("jihe")){
                jhFlag="2";
            }
        }
        if(jhFlag.equals("1")&&kjFlag.equals("2")){
            vo.setAccounting(userInfo.getId()+"");
        }

        String startDate=vo.getStartDate();
        vo.setStartDate(null);
        String endDate=vo.getEndDate();
        vo.setEndDate(null);
        String corporateName=null;
        if(vo.getCustomerName()!=null&&(!vo.getCustomerName().equals("undefined"))) {
            corporateName = vo.getCustomerName();
        }
        vo.setCustomerName(null);
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        if(startDate!=null&&startDate.length()>0) {
            queryWrapper.between("sign_time", startDate, endDate);
        }
        queryWrapper.orderByDesc("id");
        String[] sts=status.split(",");
        queryWrapper.in("status",Arrays.asList(sts));
        List list2 = contractInfoService.list(queryWrapper);
        List list=new ArrayList();
        for(Object obj:list2){
            ContractInfo info=(ContractInfo)obj;

            ContractInfoVo entity = GemBeanUtils.copyProperties(info, ContractInfoVo.class);
            User user=userService.getById(entity.getSignedBy());
            User user1=userService.getById(entity.getAccounting());
            if(user!=null) {
                entity.setSignedName(user.getRealname());
            }
            if(entity.getAccounting()!=null&&entity.getAccounting()!=""&&user1!=null) {
                entity.setAccountingName(user1.getRealname());
                if(user1.getLevel()!=null&&user1.getLevel().length()>0) {
                    BookingSettings settingsbooking = bookingSettingsService.getById(user1.getLevel());
                    entity.setLevelName(settingsbooking.getLevenName());
                }
            }

            if(info.getCustomerId()!=null) {
                CustomerResearch customerResearch = customerResearchService.getById(info.getCustomerId());
                if (corporateName != null && customerResearch.getCorporateName().indexOf(corporateName) == -1) {
                    continue;
                }
                entity.setCustomerName(customerResearch.getCorporateName());
            }
            AssociatedServicesVo servicesVo=new AssociatedServicesVo();
            servicesVo.setContractId(entity.getId()+"");
            servicesVo.setType("0");
            QueryWrapper wrapper = makeQueryMaps(servicesVo);
            List<AssociatedServices> list1=associatedServicesService.list(wrapper);
            entity.setServicesList(list1);
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
            if(corporateName!=null&&corporateName.length()>0){
                if(entity.getCustomerName().indexOf(corporateName)!=-1){
                    list.add(entity);
                }
            }else{
                list.add(entity);
            }


        }

        Map map=new HashMap();
        map.put("list",list);

        FeeReceivableVo receivable=new FeeReceivableVo();
        receivable.setCustomerName(corporateName);
        receivable.setType("2");
        receivable.setStatus("1");
        map.put("backFlag","false");
        QueryWrapper queryWrapper1=makeQueryMaps(receivable);
        List<FeeReceivable> list3=feeReceivableService.list(queryWrapper1);
        BigDecimal totalMoney=new BigDecimal(0);
        if(list3.size()>0) {
            for (FeeReceivable vable : list3) {
                totalMoney = totalMoney.add(vable.getMoney());
            }
            map.put("backFlag","true");
            map.put("backMoney",totalMoney);
        }

        return BaseResultData.SUCCESS(map);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("contractInfo:save")
    public BaseResultData save(@RequestBody ContractInfoVo vo) {
        User userInfo=(User)SecurityUtils.getSubject().getSession().getAttribute(CURRENT_USER_KEY);
        vo.setSignedBy(userInfo.getId()+"");
        String ctId=null;
        //判断合同编号
        int  flagCount=1;
        while (flagCount!=0){
            ctId=(String)getContractId().getData();
            ContractInfoVo contractInfoVoPd=new ContractInfoVo();
            contractInfoVoPd.setContractId(ctId);
            QueryWrapper queryWrapperpd = makeQueryMaps(contractInfoVoPd);
            flagCount= contractInfoService.count(queryWrapperpd);
        }
        String services=null;
        if(vo.getServices()!=null&&vo.getServices()!=""){
            services=vo.getServices();
            vo.setServices(null);
        }
        vo.setContractId(ctId);
        if(vo.getStatus()==null) {
            vo.setStatus("0");
        }
        vo.setOther2("0");
        CustomerResearchVo researchVo=new CustomerResearchVo();
        researchVo.setCorporateName(vo.getCustomerName());
        QueryWrapper queryWrapper = makeQueryMaps(researchVo);
        List<CustomerResearch> cuList = customerResearchService.list(queryWrapper);
        if(cuList.size()>0){
            vo.setCustomerId(cuList.get(0).getId());
        }else{
            CustomerResearchVo researchVo1 =new CustomerResearchVo();
            researchVo1.setCorporateName(vo.getCustomerName());
            GemValidate(researchVo1, SaveValidator.class);
            CustomerResearch research = GemBeanUtils.copyProperties(researchVo1, CustomerResearch.class);
            if(!customerResearchService.save(research)){
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        }
        GemValidate(vo, SaveValidator.class);
        ContractInfo entity = GemBeanUtils.copyProperties(vo, ContractInfo.class);
        if(!contractInfoService.save(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
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
                associatedServices.setType("0");
                GemValidate(associatedServices, SaveValidator.class);
                if (!associatedServicesService.save(associatedServices)) {
                    return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
                }
            }
        }
        if(vo.getServiceId()!=null&&vo.getServiceId().length()>0) {
            BusinessCharges businessCharges=businessChargesService.getById(vo.getServiceId());
            FeeReceivableVo receivable = new FeeReceivableVo();
            receivable.setGuanLianId(businessCharges.getServiceId());
            QueryWrapper queryWrapper1 = makeQueryMaps(receivable);
            queryWrapper1.in("type","0","1");
            FeeReceivable feeReceivable = feeReceivableService.getOne(queryWrapper1);
            BigDecimal total=feeReceivable.getMoney().add(vo.getAmountReceived());
            feeReceivable.setMoney(total);
            if (!feeReceivableService.updateById(feeReceivable)) {
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        }else{
            FeeReceivable receivable = new FeeReceivable();
            receivable.setCustomerId(entity.getCustomerId() == null ? null : entity.getCustomerId() + "");
            receivable.setCustomerName(entity.getCustomerName());
            receivable.setGuanLianId(entity.getContractId());
            receivable.setMoney(entity.getAmountReceived());
            receivable.setType("0");
            receivable.setStatus("0");
            receivable.setPayType(entity.getPayType());
            receivable.setUserId(entity.getSignedBy());
            GemValidate(receivable, SaveValidator.class);
            if (!feeReceivableService.save(receivable)) {
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        }

        if(vo.getBackMoney()!=null){
            FeeReceivableVo receivable=new FeeReceivableVo();
            receivable.setCustomerName(vo.getCustomerName());
            receivable.setType("2");
            receivable.setStatus("1");
            QueryWrapper queryWrapper1=makeQueryMaps(receivable);
            List<FeeReceivable> list3=feeReceivableService.list(queryWrapper1);
            for(FeeReceivable vable:list3){
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
    @RequiresPermissions("contractInfo:delete")
    public BaseResultData delete(Long id,String ids) {

        if(id!=null){
            ContractInfo contractInfo=contractInfoService.getById(id);
            contractInfoService.removeById(id);
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("guan_lian_id",contractInfo.getContractId());
            List<FeeReceivable> list=feeReceivableService.list(wrapper);
            for(FeeReceivable receivable:list){
                feeReceivableService.removeById(receivable.getId());
            }
        };
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                for(Long reId:listIds){
                    ContractInfo contractInfo=contractInfoService.getById(reId);
                    QueryWrapper wrapper=new QueryWrapper();
                    wrapper.eq("guan_lian_id",contractInfo.getContractId());
                    List<FeeReceivable> list=feeReceivableService.list(wrapper);
                    for(FeeReceivable receivable:list){
                        feeReceivableService.removeById(receivable.getId());
                    }
                }
                contractInfoService.removeByIds(listIds);

            }
        }

        return BaseResultData.SUCCESS();
    }

    @PostMapping("/paigong")
    public BaseResultData paigong(@RequestBody ContractInfoVo vo){
        ContractInfo info=new ContractInfo();
        info.setAccounting(vo.getAccounting());
        if(vo.getId()!=null){
            info.setId(vo.getId());
            contractInfoService.updateById(info);
        }
        if(StringUtils.isNotBlank(vo.getIds())){
            List<Long> listIds = Arrays.asList(vo.getIds().split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                for(Long id:listIds){
                    info.setId(id);
                    contractInfoService.updateById(info);
                }
            }
        }
        return BaseResultData.SUCCESS();
    }

    @Log(type = OperateType.ALTER,value = "续签"+moduleName)
    @PostMapping("/xuqian")
    @RequiresPermissions("contractInfo:xuqian")
    public BaseResultData xuqian(@RequestBody ContractInfoVo vo) {
        User userInfo=(User)SecurityUtils.getSubject().getSession().getAttribute(CURRENT_USER_KEY);
        vo.setSignedBy(userInfo.getId()+"");
        String ctId=null;
        //判断合同编号
        int  flagCount=1;
        while (flagCount!=0){
            ctId=(String)getContractId().getData();
            ContractInfoVo contractInfoVoPd=new ContractInfoVo();
            contractInfoVoPd.setContractId(ctId);
            QueryWrapper queryWrapperpd = makeQueryMaps(contractInfoVoPd);
            flagCount= contractInfoService.count(queryWrapperpd);
        }
        String services=null;
        if(vo.getServices()!=null&&vo.getServices()!=""){
            services=vo.getServices();
            vo.setServices(null);
        }
        vo.setContractId(ctId);
        if(vo.getStatus()==null) {
            vo.setStatus("1");
        }
        vo.setOther2("0");
        CustomerResearchVo researchVo=new CustomerResearchVo();
        researchVo.setCorporateName(vo.getCustomerName());
        QueryWrapper queryWrapper = makeQueryMaps(researchVo);
        List<CustomerResearch> cuList = customerResearchService.list(queryWrapper);
        if(cuList.size()>0){
            vo.setCustomerId(cuList.get(0).getId());
        }else{
            CustomerResearchVo researchVo1 =new CustomerResearchVo();
            researchVo1.setCorporateName(vo.getCustomerName());
            GemValidate(researchVo1, SaveValidator.class);
            CustomerResearch research = GemBeanUtils.copyProperties(researchVo1, CustomerResearch.class);
            if(!customerResearchService.save(research)){
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        }
        GemValidate(vo, SaveValidator.class);
        ContractInfo entity = GemBeanUtils.copyProperties(vo, ContractInfo.class);
        if(!contractInfoService.save(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
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
                associatedServices.setType("0");
                GemValidate(associatedServices, SaveValidator.class);
                if (!associatedServicesService.save(associatedServices)) {
                    return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
                }
            }
        }
        if(vo.getServiceId()!=null&&vo.getServiceId().length()>0) {
            BusinessCharges businessCharges=businessChargesService.getById(vo.getServiceId());
            FeeReceivableVo receivable = new FeeReceivableVo();
            receivable.setGuanLianId(businessCharges.getServiceId());
            QueryWrapper queryWrapper1 = makeQueryMaps(receivable);
            queryWrapper1.in("type","0","1");
            FeeReceivable feeReceivable = feeReceivableService.getOne(queryWrapper1);
            BigDecimal total=feeReceivable.getMoney().add(vo.getAmountReceived());
            feeReceivable.setMoney(total);
            if (!feeReceivableService.updateById(feeReceivable)) {
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        }else{
            FeeReceivable receivable = new FeeReceivable();
            receivable.setCustomerId(entity.getCustomerId() == null ? null : entity.getCustomerId() + "");
            receivable.setCustomerName(entity.getCustomerName());
            receivable.setGuanLianId(entity.getContractId());
            receivable.setMoney(entity.getAmountReceived());
            receivable.setType("0");
            receivable.setStatus("0");
            receivable.setPayType(entity.getPayType());
            receivable.setUserId(entity.getSignedBy());
            GemValidate(receivable, SaveValidator.class);
            if (!feeReceivableService.save(receivable)) {
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        }

        if(vo.getBackMoney()!=null){
            FeeReceivableVo receivable=new FeeReceivableVo();
            receivable.setCustomerName(vo.getCustomerName());
            receivable.setType("2");
            receivable.setStatus("1");
            QueryWrapper queryWrapper1=makeQueryMaps(receivable);
            List<FeeReceivable> list3=feeReceivableService.list(queryWrapper1);
            for(FeeReceivable vable:list3){
                vable.setStatus("3");
                feeReceivableService.updateById(vable);
            }
        }


        return BaseResultData.SUCCESS(entity);
    }

    @Log(type = OperateType.ALTER,value = "重新提交"+moduleName)
    @PostMapping("/cxtj")
    @RequiresPermissions("contractInfo:update")
    public BaseResultData cxtj(@RequestBody ContractInfoVo vo) {
        ContractInfo oldInfo=contractInfoService.getById(vo.getId());
        String services=null;
        if(vo.getServices()!=null&&vo.getServices()!=""){
            services=vo.getServices();
            vo.setServices(null);
        }
        vo.setOther2("0");
        GemValidate(vo, UpdateValidator.class);
        ContractInfo entity = GemBeanUtils.copyProperties(vo, ContractInfo.class);
        if(!contractInfoService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        AssociatedServicesVo servicesVo=new AssociatedServicesVo();
        servicesVo.setType("0");
        servicesVo.setContractId(entity.getId()+"");
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
                GemValidate(associatedServices, SaveValidator.class);
                if (!associatedServicesService.save(associatedServices)) {
                    return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
                }
            }
        }

        if(vo.getServiceId()!=null&&vo.getServiceId().length()>0) {
            BusinessCharges businessCharges=businessChargesService.getById(vo.getServiceId());
            FeeReceivableVo receivable = new FeeReceivableVo();
            receivable.setGuanLianId(businessCharges.getServiceId());
            QueryWrapper queryWrapper1 = makeQueryMaps(receivable);
            queryWrapper1.in("type","0","1");
            FeeReceivable feeReceivable = feeReceivableService.getOne(queryWrapper1);
            BigDecimal total=feeReceivable.getMoney().add(vo.getAmountReceived());
            total=total.subtract(oldInfo.getAmountReceived());
            feeReceivable.setMoney(total);
            if (!feeReceivableService.updateById(feeReceivable)) {
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        }else {
            FeeReceivableVo feeReceivableVo = new FeeReceivableVo();
            feeReceivableVo.setGuanLianId(entity.getContractId());
            QueryWrapper queryWrapper = makeQueryMaps(feeReceivableVo);
            queryWrapper.in("type","0","1");
            List<FeeReceivable> list = feeReceivableService.list(queryWrapper);
            if (list.size() > 0) {
                FeeReceivable receivable = list.get(0);
                receivable.setMoney(entity.getAmountReceived());
                receivable.setCustomerName(entity.getCustomerName());
                receivable.setCustomerId(entity.getCustomerId() + "");
                receivable.setUserId(entity.getSignedBy());
                receivable.setPayType(entity.getPayType());
                receivable.setStatus("0");
                if (!feeReceivableService.updateById(receivable)) {
                    return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
                }
            }
        }

        return BaseResultData.SUCCESS(entity);
    }


    @Log(type = OperateType.ALTER,value = "补收费用"+moduleName)
    @PostMapping("/bushouFee")
    public BaseResultData bushouFee(@RequestBody FeeReceivableVo vo) {
        User userInfo=(User)SecurityUtils.getSubject().getSession().getAttribute(CURRENT_USER_KEY);
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("contract_id",vo.getGuanLianId());
        ContractInfo info=contractInfoService.getOne(wrapper);
        vo.setStatus("0");
        vo.setType("3");
        vo.setCustomerName(info.getCustomerName());
        vo.setUserId(userInfo.getId()+"");
        vo.setCreateTime(new Date());
        GemValidate(vo, SaveValidator.class);
        FeeReceivable entity = GemBeanUtils.copyProperties(vo, FeeReceivable.class);
        if(!feeReceivableService.save(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }

        return BaseResultData.SUCCESS(entity);
    }
    /**
     * 编辑
     * @return
     */
    @Log(type = OperateType.ALTER,value = "停止合同"+moduleName)
    @PostMapping("/tingzhi")
    @RequiresPermissions("contractInfo:update")
    public BaseResultData tingzhi(@RequestBody ContractInfoVo vo) {
        vo.setServiceEndTime(vo.getTingzhiTime());
        GemValidate(vo, UpdateValidator.class);
        ContractInfo info=contractInfoService.getById(vo.getId());
        ContractInfo entity = GemBeanUtils.copyProperties(vo, ContractInfo.class);
        if(!contractInfoService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        AssociatedServicesVo servicesVo=new AssociatedServicesVo();
        servicesVo.setContractId(info.getId()+"");
        servicesVo.setType("0");
        QueryWrapper wrapper = makeQueryMaps(servicesVo);
        List<AssociatedServices> list1=associatedServicesService.list(wrapper);
        //计算退款金额
        if(list1.size()>0&&StringUtils.isNotBlank(info.getChargingStandard())&&!StringUtils.equals("0",info.getChargingStandard())
        &&info.getServiceEndTime().compareTo(vo.getTingzhiTime())==1) {
            BigDecimal total = new BigDecimal(0);
            for (AssociatedServices services : list1) {
                total = total.add(services.getPrice());
            }
            FeeReceivable feeReceivable = new FeeReceivable();
            feeReceivable.setPayType(info.getPayType());
            feeReceivable.setStatus("0");
            feeReceivable.setType("2");
            feeReceivable.setGuanLianId(info.getContractId());
            feeReceivable.setCustomerId(info.getCustomerId() + "");
            feeReceivable.setCustomerName(info.getCustomerName());
            feeReceivable.setUserId(info.getSignedBy());
            boolean flag = false;
            int yyys = getMonthDiff(info.getServiceStartTime(), vo.getTingzhiTime())-1;
            if (info.getChargingStandard().equals("1")) {
                //季
                total = total.divide(new BigDecimal(3), 2, BigDecimal.ROUND_HALF_UP);
                if (yyys % 3 != 0) {
                    Integer sy = yyys > 3 ? 3 - (yyys % 3)   : 3 - yyys;
                    BigDecimal money = total.multiply(new BigDecimal(sy));
                    feeReceivable.setMoney(money);
                    flag = true;
                }
            } else if (info.getChargingStandard().equals("2")) {
                total = total.divide(new BigDecimal(6), 2, BigDecimal.ROUND_HALF_UP);
                //半年
                if (yyys % 6 != 0) {
                    Integer sy = yyys > 6 ? 6 - (yyys % 6) : 6 - yyys;
                    BigDecimal money = total.multiply(new BigDecimal(sy));
                    feeReceivable.setMoney(money);
                    flag = true;
                }

            } else if (info.getChargingStandard().equals("3")) {
                total = total.divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP);
                //年
                if (yyys % 12 != 0) {

                    Integer sy = yyys > 12 ? 12 - (yyys % 12) : 12 - yyys;
                    BigDecimal money = total.multiply(new BigDecimal(sy));
                    feeReceivable.setMoney(money);
                    flag = true;
                }

            }
            if (flag) {
                feeReceivableService.save(feeReceivable);
            }
        }
        return BaseResultData.SUCCESS(entity);
    }

    @Log(type = OperateType.ALTER,value = "修改发票状态"+moduleName)
    @PostMapping("/updatefpStatus")
    @RequiresPermissions("contractInfo:update")
    public BaseResultData updatefpStatus(@RequestBody ContractInfoVo vo)  {
        GemValidate(vo, UpdateValidator.class);
        ContractInfo entity = GemBeanUtils.copyProperties(vo, ContractInfo.class);
        if(!contractInfoService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return  BaseResultData.SUCCESS(entity);
    }

    /**
     * 编辑
     * @return
     */
    @Log(type = OperateType.ALTER,value = "编辑"+moduleName)
    @PostMapping("/update")
    @RequiresPermissions("contractInfo:update")
    public BaseResultData update(@RequestBody ContractInfoVo vo)  {
        ContractInfo oldInfo=contractInfoService.getById(vo.getId());
        String services=null;
        if(vo.getServices()!=null&&vo.getServices()!=""){
            services=vo.getServices();
            vo.setServices(null);
        }

        //判断某个日期是否在两个日期范围之内
        if(vo.getServiceStartTime().compareTo(new Date())==-1 && vo.getServiceEndTime().compareTo(new Date())==1){
            vo.setStatus("2");
        }


        GemValidate(vo, UpdateValidator.class);
        ContractInfo entity = GemBeanUtils.copyProperties(vo, ContractInfo.class);
        if(!contractInfoService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        AssociatedServicesVo servicesVo=new AssociatedServicesVo();
        servicesVo.setContractId(entity.getId()+"");
        servicesVo.setType("0");
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
                GemValidate(associatedServices, SaveValidator.class);
                if (!associatedServicesService.save(associatedServices)) {
                    return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
                }
            }
        }

        if(vo.getServiceId()!=null&&vo.getServiceId().length()>0) {
            BusinessCharges businessCharges=businessChargesService.getById(vo.getServiceId());
            FeeReceivableVo receivable = new FeeReceivableVo();
            receivable.setGuanLianId(businessCharges.getServiceId());
            QueryWrapper queryWrapper1 = makeQueryMaps(receivable);
            queryWrapper1.in("type","0","1");
            FeeReceivable feeReceivable = feeReceivableService.getOne(queryWrapper1);
            BigDecimal total=feeReceivable.getMoney().add(vo.getAmountReceived());
            total=total.subtract(oldInfo.getAmountReceived());
            feeReceivable.setMoney(total);
            if (!feeReceivableService.updateById(feeReceivable)) {
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        }else {
            FeeReceivableVo feeReceivableVo = new FeeReceivableVo();
            feeReceivableVo.setGuanLianId(entity.getContractId());
            QueryWrapper queryWrapper = makeQueryMaps(feeReceivableVo);
            queryWrapper.in("type","0","1");
            List<FeeReceivable> list = feeReceivableService.list(queryWrapper);
            if (list.size() > 0) {
                FeeReceivable receivable = list.get(0);
                receivable.setMoney(entity.getAmountReceived());
                receivable.setCustomerName(entity.getCustomerName());
                receivable.setCustomerId(entity.getCustomerId() + "");
                receivable.setUserId(entity.getSignedBy());
                receivable.setPayType(entity.getPayType());
                if (!feeReceivableService.updateById(receivable)) {
                    return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
                }
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
    @RequiresPermissions("contractInfo:info")
    public BaseResultData info(Long id) {
        ContractInfo info = contractInfoService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

    @GetMapping("/userRoles/list")
    public BaseResultData list(UserRolesVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List<UserRoles> list = userRolesService.list(queryWrapper);
        List<User> userList=new ArrayList<>();
        for(UserRoles roles:list){
            User user=userService.getById(roles.getUserId());
            userList.add(user);
        }
        return BaseResultData.SUCCESS(userList);
    }

    @GetMapping("/customerResearch/list")
    public BaseResultData list(CustomerResearchVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = customerResearchService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    @GetMapping("/countCus")
    public BaseResultData countCus(ContractInfoVo vo){
        QueryWrapper<ContractInfo> queryWrapper=makeQueryMaps(vo);
        User userInfo=(User)SecurityUtils.getSubject().getSession().getAttribute(CURRENT_USER_KEY);
        List<UserRoles> userRoles=userRolesService.findByUserId(userInfo.getId());
        String kjFlag="1";
        String jhFlag="1";
        for(UserRoles roles:userRoles){
            Role role=roleService.getById(roles.getRoleId());
            if(role.getFlag().equals("kuaiji")){

                kjFlag="2";
            }
            if(role.getFlag().equals("jihe")){
                jhFlag="2";
            }
        }

        if(jhFlag.equals("1")&&kjFlag.equals("2")){
            String userId=userInfo.getId().toString();
            queryWrapper.and(wrapper1 -> wrapper1.eq("signed_by",userId).or().eq("accounting",userId));
        }
        queryWrapper.orderByDesc("service_end_time");
        List<ContractInfo> list=contractInfoService.list(queryWrapper);

        int zc=0;
        int th=0;
        int zx=0;
        int zz=0;
        int xgm=0;
        int ybnsr=0;
        int gth=0;
        int gthnsr=0;
        int jzhs=0;
        Set set=new HashSet();
        Set zcSet=new HashSet();
        Set thSet=new HashSet();
        Set zxSet=new HashSet();
        Set zzSet=new HashSet();
        Set xgmSet=new HashSet();
        Set ybnsrSet=new HashSet();
        Set gthSet=new HashSet();
        Set gthnsrSet=new HashSet();
        for(ContractInfo info:list){
            if(jhFlag.equals("1")&&kjFlag.equals("2")) {
                if (StringUtils.isNotBlank(info.getAccounting()) && !StringUtils.equals(info.getAccounting(), userInfo.getId().toString())) {
                    continue;
                }
            }
            if(info.getStatus()==null){
                continue;
            }
            if(info.getStatus().equals("2")||info.getStatus().equals("1")||info.getStatus().equals("0")){
                zc+=1;
                zcSet.add(info.getCustomerName());
            }



            if(info.getContractNature().equals("0")){
                xgm+=1;
                xgmSet.add(info.getCustomerName());
            }else if(info.getContractNature().equals("1")){
                ybnsr+=1;
                ybnsrSet.add(info.getCustomerName());
            }else if(info.getContractNature().equals("2")){
                gth+=1;
                gthSet.add(info.getCustomerName());
            }else if(info.getContractNature().equals("3")){
                gthnsr+=1;
                gthnsrSet.add(info.getCustomerName());
            }
            set.add(info.getCustomerName());
            jzhs+=1;
        }
        for(ContractInfo info:list){
            if(info.getStatus().equals("4")||info.getStatus().equals("3")){
                if(!zcSet.contains(info.getCustomerName())) {
                    thSet.add(info.getCustomerName());
                }
                th+=1;
            }
        }
        for(ContractInfo info:list){
            if(info.getStatus().equals("5")){
                if(!zcSet.contains(info.getCustomerName())&&!thSet.contains(info.getCustomerName())) {
                    zzSet.add(info.getCustomerName());
                }
                zz+=1;
            }
        }

        for(ContractInfo info:list) {
            if (info.getStatus().equals("6")) {
                if (!zcSet.contains(info.getCustomerName())&&!thSet.contains(info.getCustomerName())&&!zzSet.contains(info.getCustomerName())) {
                    zxSet.add(info.getCustomerName());
                }
                zx += 1;
            }
        }
        Map map=new HashMap();
        map.put("jzhs",set.size());
        map.put("zc",zcSet.size());
        map.put("th",thSet.size());
        map.put("zx",zxSet.size());
        map.put("zz",zzSet.size());
        map.put("xgm",xgmSet.size());
        map.put("ybnsr",ybnsrSet.size());
        map.put("gth",gthSet.size());
        map.put("gthnsr",gthnsrSet.size());
        return BaseResultData.SUCCESS(map);
    }

    @GetMapping("/getContractId")
    public BaseResultData getContractId(){
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String date = f.format(new Date());
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("deleted","0");
        queryWrapper.like("contract_id","H"+date);
        queryWrapper.orderByDesc("id");
        List<ContractInfo> list=contractInfoService.list(queryWrapper);
        if(list.size()==0){
            date="H"+date+"0001";
        }else{
            String id=list.get(0).getContractId();
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
            date="H"+date+sesId;
        }
        return BaseResultData.SUCCESS(date);
    }


    /**
     * 获取两个日期相差的月数
     */
    public static int getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        int monthsDiff = Math.abs(yearInterval * 12 + monthInterval);
        return monthsDiff;
    }



}