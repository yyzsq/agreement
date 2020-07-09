
package com.gemframework.controller.extend.record;

import com.aliyun.oss.common.utils.DateUtil;
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
import com.gemframework.model.entity.vo.ContractInfoVo;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import com.gemframework.model.entity.vo.HandoverRecordVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.gemframework.common.constant.GemSessionKeys.CURRENT_USER_KEY;

/**
 * @Title: HandoverRecordController
 * @Date: 2020-05-15 10:31:46
 * @Version: v1.0
 * @Description: 交接记录控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/record/handoverRecord")
public class HandoverRecordController extends BaseController {

    private static final String moduleName = "交接记录";

    @Autowired
    private HandoverRecordService handoverRecordService;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerResearchService customerResearchService;
    @Autowired
    private ContractInfoService contractInfoService;
    @Autowired
    private MonthlyReportService monthlyReportService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("handoverRecord:page")
    public BaseResultData page(PageInfo pageInfo, HandoverRecordVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        queryWrapper.orderByDesc("create_time");
        Page page = handoverRecordService.page(setOrderPage(pageInfo),queryWrapper);
        List list=new ArrayList();
        for(Object obj:page.getRecords()){
            HandoverRecord hr=(HandoverRecord)obj;
            HandoverRecordVo hrv = GemBeanUtils.copyProperties(hr, HandoverRecordVo.class);
            User user1= userService.getById(hrv.getAccounting());
            User user2= userService.getById(hr.getReceiver());
            CustomerResearch firm= customerResearchService.getById(hr.getCustomerId());
            if(user1!=null)
            hrv.setDeliverPersonName(user1.getRealname());
            if(user2!=null)
            hrv.setReceiverName(user2.getRealname());
            if(firm!=null)
            hrv.setCustomerName(firm.getCorporateName());
            list.add(hrv);
        }
        return BaseResultData.SUCCESS(list,page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("handoverRecord:list")
    public BaseResultData list(HandoverRecordVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List<HandoverRecord> list = handoverRecordService.list(queryWrapper);
        List<HandoverRecordVo> list1=new ArrayList();
        for(HandoverRecord record:list){
            HandoverRecordVo entity2 = GemBeanUtils.copyProperties(record, HandoverRecordVo.class);
            User user=userService.getById(entity2.getReceiver());
            entity2.setReceiverName(user.getRealname());
            list1.add(entity2);
        }
        return BaseResultData.SUCCESS(list1);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    public BaseResultData save(@RequestBody HandoverRecordVo vo) {
        vo.setContractId(vo.getId()+"");
        vo.setId(null);
        vo.setCreateTime(new Date());
        vo.setStatus("0");
        GemValidate(vo, SaveValidator.class);
        HandoverRecord entity = GemBeanUtils.copyProperties(vo, HandoverRecord.class);
        if(!handoverRecordService.save(entity)) {
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        ContractInfo contractInfo=new ContractInfo();
        contractInfo.setId(Long.parseLong(vo.getContractId()));
        contractInfo.setOther1("0");
        GemValidate(contractInfo, UpdateValidator.class);
        ContractInfo entity2 = GemBeanUtils.copyProperties(contractInfo, ContractInfo.class);
        if(!contractInfoService.updateById(entity2)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }


    /**
     * 删除 & 批量刪除
     * @return
     */
    @Log(type = OperateType.ALTER,value = "删除"+moduleName)
    @PostMapping("/delete")
    @RequiresPermissions("handoverRecord:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) handoverRecordService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    handoverRecordService.removeByIds(listIds);
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
    @RequiresPermissions("handoverRecord:update")
    public BaseResultData update(@RequestBody HandoverRecordVo vo) {
        GemValidate(vo, UpdateValidator.class);
        HandoverRecord entity = GemBeanUtils.copyProperties(vo, HandoverRecord.class);
        if(!handoverRecordService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        entity=handoverRecordService.getById(entity.getId());
        ContractInfo contractInfo=new ContractInfo();
        contractInfo.setId(Long.parseLong(entity.getContractId()));
        contractInfo.setAccounting(entity.getReceiver());
        if(entity.getStatus().equals("1")) {
            contractInfo.setOther1("1");

        }
        contractInfoService.updateById(contractInfo);
        if(entity.getStatus().equals("1")) {
            String customerNam="";
            if(entity.getOther2()!=null){
                customerNam=entity.getOther2();
            }else {
                CustomerResearch research=customerResearchService.getById(entity.getCustomerId());
                customerNam=research.getCorporateName();
            }
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("customer_name",customerNam);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-01");
            wrapper.gt("create_time",sdf2.format(new Date()));
            List<MonthlyReport> list=monthlyReportService.list(wrapper);
            if(list.size()>0){
                MonthlyReport report=list.get(0);
                report.setUserId(entity.getReceiver());
                monthlyReportService.updateById(report);
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
    @RequiresPermissions("handoverRecord:info")
    public BaseResultData info(Long id) {
        HandoverRecord info = handoverRecordService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

}