
package com.gemframework.controller.extend.customer;

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
import com.gemframework.utils.DefaultInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: CustomerResearchController
 * @Date: 2020-05-10 17:39:43
 * @Version: v1.0
 * @Description: 客户信息表控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/customer/customerResearch")
public class CustomerResearchController extends BaseController {

    private static final String moduleName = "客户信息表";

    @Autowired
    private CustomerResearchService customerResearchService;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private MonthlyReportService monthlyReportService;
    @Autowired
    private FeeReceivableService feeReceivableService;
    @Autowired
    private ServiceSettingsService serviceSettingsService;
    @Autowired
    private ContractInfoService contractInfoService;
    @Autowired
    private BusinessChargesService businessChargesService;
    @Autowired
    private WechatNoticeService wechatNoticeService;
    @Autowired
    private AssociatedServicesService associatedServicesService;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("customerResearch:page")
    public BaseResultData page(PageInfo pageInfo, CustomerResearchVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = customerResearchService.page(setOrderPage(pageInfo),queryWrapper);
        List list=new ArrayList();
        for(Object obj:page.getRecords()){
            CustomerResearch cr=(CustomerResearch)obj;
            CustomerResearchVo entity = GemBeanUtils.copyProperties(cr, CustomerResearchVo.class);
            UploadFileVo uploadFile=new UploadFileVo();
            uploadFile.setRelationId(cr.getId()+"");
            QueryWrapper query = makeQueryMaps(uploadFile);
            List<UploadFile> fjList= uploadFileService.list(query);
            if(fjList.size()!=0) {
                String html="";
                for(UploadFile file:fjList) {
                    html+=(DefaultInfo.fileType.get(file.getImgType())+ ":<a style='color:blue;'  target='_blank' href='" + file.getImgUrl() + "'>" + file.getImgName() + "</a>&nbsp;&nbsp;");
                }
                entity.setEnclosure(html);
            }
            entity.setFjList(fjList);
            list.add(entity);
        }
        return BaseResultData.SUCCESS(list,page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("customerResearch:list")
    public BaseResultData list(CustomerResearchVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = customerResearchService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("customerResearch:save")
    public BaseResultData save(@RequestBody CustomerResearchVo vo) {

        GemValidate(vo, SaveValidator.class);
        CustomerResearch entity = GemBeanUtils.copyProperties(vo, CustomerResearch.class);
        if(!customerResearchService.save(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        if(vo.getFujianList()!=null&&vo.getFujianList().length()>0) {
            JSONArray array = JSONObject.parseArray(vo.getFujianList());
            addUpload(array, entity.getId() + "");
        }
        return BaseResultData.SUCCESS(entity);
    }


    /**
     * 删除 & 批量刪除
     * @return
     */
    @Log(type = OperateType.ALTER,value = "删除"+moduleName)
    @PostMapping("/delete")
    @RequiresPermissions("customerResearch:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) customerResearchService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    customerResearchService.removeByIds(listIds);
            }
        }
        return BaseResultData.SUCCESS();
    }


    /**
     * 编辑
     * @return
     */
    @Log(type = OperateType.ALTER,value = "编辑"+moduleName)
    @PostMapping("/update")
    @RequiresPermissions("customerResearch:update")
    public BaseResultData update(@RequestBody CustomerResearchVo vo) {
        GemValidate(vo, UpdateValidator.class);
        CustomerResearch entity = GemBeanUtils.copyProperties(vo, CustomerResearch.class);

        //修改合同
        //CustomerResearch 客户资料
        //ContractInfo  合同
        CustomerResearch research = customerResearchService.getById(entity.getId());
        if (!customerResearchService.updateById(entity)) {
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("customer_name", research.getCorporateName());
        List<ContractInfo> infoList = contractInfoService.list(queryWrapper);
        for (ContractInfo info : infoList) {
            info.setCustomerName(entity.getCorporateName());
            contractInfoService.updateById(info);
        }
        //修改业务资料

        QueryWrapper businessWrapper=new QueryWrapper();
        businessWrapper.eq("customer_name",research.getCorporateName());
        List<BusinessCharges> businessCharges=businessChargesService.list(businessWrapper);
        for (BusinessCharges charges : businessCharges){
            charges.setCustomerName(entity.getCorporateName());
            businessChargesService.updateById(charges);
        }

        //修改收费管理

        QueryWrapper feeWrapper=new QueryWrapper();
        feeWrapper.eq("customer_name",research.getCorporateName());
        List<FeeReceivable> feelist=feeReceivableService.list(feeWrapper);
        for (FeeReceivable fee : feelist){
            fee.setCustomerName(entity.getCorporateName());
            feeReceivableService.updateById(fee);
        }
        //修改月报

        QueryWrapper monthWrapper=new QueryWrapper();
        monthWrapper.eq("customer_name",research.getCorporateName());
        List<MonthlyReport> monthlist=monthlyReportService.list(monthWrapper);
        for (MonthlyReport report : monthlist){
            report.setCustomerName(entity.getCorporateName());
            monthlyReportService.updateById(report);
        }

        //修改微信绑定管理

        QueryWrapper wechatWrapper=new QueryWrapper();
        wechatWrapper.eq("customer_name",research.getCorporateName());
        List<WechatNotice> wechatlist=wechatNoticeService.list(wechatWrapper);
        for (WechatNotice notice : wechatlist){
            notice.setCustomerName(entity.getCorporateName());
            wechatNoticeService.updateById(notice);
        }

        if (vo.getFujianList() != null) {
            UploadFileVo old = new UploadFileVo();
            old.setRelationId(vo.getId() + "");
            QueryWrapper wrapper = makeQueryMaps(old);
            uploadFileService.remove(wrapper);
            JSONArray array = JSONObject.parseArray(vo.getFujianList());
            System.out.println(vo.getFujianList());
            addUpload(array, vo.getId() + "");
        }




        return BaseResultData.SUCCESS(entity);
    }

    public void addUpload(JSONArray array,String id ){
        for(Object obj:array){
            JSONObject json=(JSONObject)obj;
            UploadFile upload=new UploadFile();
            upload.setImgName(json.getString("imgName"));
            upload.setImgType(json.getString("imgType"));
            upload.setImgUrl(json.getString("imgUrl"));
            upload.setRelationId(id);
            uploadFileService.save(upload);
        }
    }


    /**
     * 获取用户信息ById
     * @return
     */
    @Log(type = OperateType.NORMAL,value = "查看"+moduleName)
    @GetMapping("/info")
    @RequiresPermissions("customerResearch:info")
    public BaseResultData info(Long id) {
        CustomerResearch info = customerResearchService.getById(id);
        CustomerResearchVo entity = GemBeanUtils.copyProperties(info, CustomerResearchVo.class);
        UploadFileVo vo=new UploadFileVo();
        vo.setRelationId(id+"");
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List<UploadFile> list=uploadFileService.list(queryWrapper);
        entity.setFjList(list);
        return BaseResultData.SUCCESS(entity);
    }

    /**
     * 获取用户信息ById
     * @return
     */
    @Log(type = OperateType.NORMAL,value = "查看"+moduleName)
    @GetMapping("/infoDetail")
    public BaseResultData infoDetail(Long id) {
        CustomerResearch info = customerResearchService.getById(id);
        CustomerResearchVo entity = GemBeanUtils.copyProperties(info, CustomerResearchVo.class);
        UploadFileVo vo=new UploadFileVo();
        vo.setRelationId(id+"");
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List<UploadFile> list=uploadFileService.list(queryWrapper);
        entity.setFjList(list);

        //获取服务

        return BaseResultData.SUCCESS(entity);
    }

    @GetMapping("/getServiceDetail")
    public BaseResultData getServiceDetail(Long id) {
        List listInfo=new ArrayList();

        ContractInfoVo contractInfo=new ContractInfoVo();
        contractInfo.setCustomerId(id);
        QueryWrapper queryWrapper=makeQueryMaps(contractInfo);
        //获取合同
        List<ContractInfo> list1=contractInfoService.list(queryWrapper);
        for(ContractInfo info:list1){
            AssociatedServicesVo associatedServices=new AssociatedServicesVo();
            associatedServices.setPrice(info.getAmountMoney());
            associatedServices.setServiceName("代账服务");
            listInfo.add(associatedServices);
        }

        BusinessChargesVo businessChargesVo=new BusinessChargesVo();
        businessChargesVo.setCustomerId(id+"");
         queryWrapper=makeQueryMaps(businessChargesVo);
        List<BusinessCharges> list2=businessChargesService.list(queryWrapper);
        for(BusinessCharges charges:list2){
            AssociatedServicesVo servicesVo=new AssociatedServicesVo();
            servicesVo.setContractId(charges.getId()+"");
            queryWrapper=makeQueryMaps(servicesVo);
            List<AssociatedServices> list3=associatedServicesService.list(queryWrapper);
            for(AssociatedServices services:list3){
                ServiceSettings settings=serviceSettingsService.getById(services.getServiceId());
                AssociatedServicesVo entity = GemBeanUtils.copyProperties(services, AssociatedServicesVo.class);
                entity.setServiceName(settings.getServiceName());
                listInfo.add(entity);
            }
        }
        return BaseResultData.SUCCESS(listInfo);
    }
}