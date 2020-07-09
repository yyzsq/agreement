
package com.gemframework.controller.extend.fee;

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
import com.gemframework.model.entity.vo.BusinessChargesVo;
import com.gemframework.model.entity.vo.ContractInfoVo;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import com.gemframework.model.entity.vo.FeeReceivableVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

/**
 * @Title: FeeReceivableController
 * @Date: 2020-05-20 20:27:17
 * @Version: v1.0
 * @Description: 收费管理控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/fee/feeReceivable")
public class FeeReceivableController extends BaseController {

    private static final String moduleName = "收费管理";

    @Autowired
    private FeeReceivableService feeReceivableService;
    @Autowired
    private CustomerResearchService customerResearchService;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;
    @Autowired
    private BusinessChargesService businessChargesService;
    @Autowired
    private ContractInfoService contractInfoService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("feeReceivable:page")
    public BaseResultData page(PageInfo pageInfo, FeeReceivableVo vo) {
        String startTime=null;
        String endTime=null;
        if(vo.getEndDate()!=null&&vo.getEndDate().length()>0) {
            startTime=vo.getStartDate();
            endTime=vo.getEndDate();
        }
        vo.setStartDate(null);
        vo.setEndDate(null);
        Long userIds=null;
        if(StringUtils.isNotBlank(vo.getUserId())){
            userIds=Long.parseLong(vo.getUserId());
            vo.setUserId(null);
        }
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        queryWrapper.orderByDesc("create_time");
        if(userIds!=null) {
            queryWrapper.eq("user_id", userIds);
        }
        if(startTime!=null) {
            queryWrapper.between("create_time", startTime, endTime);
        }
        Page page = feeReceivableService.page(setOrderPage(pageInfo),queryWrapper);
        List list=new ArrayList();
        for(Object obj:page.getRecords()){
            FeeReceivable receivable=(FeeReceivable)obj;
            FeeReceivableVo entity = GemBeanUtils.copyProperties(receivable, FeeReceivableVo.class);
            User user=userService.getById(entity.getUserId());
            if(user!=null)
            entity.setUserName(user.getRealname());
            if(entity.getCustomerId()!=null&&entity.getCustomerId().length()>0) {
                CustomerResearch research = customerResearchService.getById(entity.getCustomerId());
                if(research!=null) {
                    entity.setCustomerName(research.getCorporateName());
                }
            }
            list.add(entity);
        }
        return BaseResultData.SUCCESS(list,page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("feeReceivable:list")
    public BaseResultData list(FeeReceivableVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = feeReceivableService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    @GetMapping("/getBackMoney")
    public BaseResultData getBackMoney(FeeReceivableVo vo){
        Map map=new HashMap();
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("type","2");
        wrapper.eq("status","1");
        wrapper.eq("customer_name",vo.getCustomerName());
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
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("feeReceivable:save")
    public BaseResultData save(@RequestBody FeeReceivableVo vo) {
        GemValidate(vo, SaveValidator.class);
        FeeReceivable entity = GemBeanUtils.copyProperties(vo, FeeReceivable.class);
        if(!feeReceivableService.save(entity)){
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
    @RequiresPermissions("feeReceivable:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) feeReceivableService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    feeReceivableService.removeByIds(listIds);
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
    @RequiresPermissions("feeReceivable:update")
    public BaseResultData update(@RequestBody FeeReceivableVo vo) {
        GemValidate(vo, UpdateValidator.class);
        FeeReceivable entity = GemBeanUtils.copyProperties(vo, FeeReceivable.class);
        if(!feeReceivableService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }

        return BaseResultData.SUCCESS(entity);
    }

    @PostMapping("/shenhe")
    public BaseResultData shenhe(@RequestBody FeeReceivableVo vo){
        GemValidate(vo, UpdateValidator.class);
        FeeReceivable entity = GemBeanUtils.copyProperties(vo, FeeReceivable.class);
        if(!feeReceivableService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        FeeReceivable feeReceivable=feeReceivableService.getById(entity.getId());
        if(feeReceivable.getType().equals("1")) {
            BusinessChargesVo businessChargesVo = new BusinessChargesVo();
            businessChargesVo.setServiceId(feeReceivable.getGuanLianId());
            QueryWrapper queryWrapper = makeQueryMaps(businessChargesVo);
            BusinessCharges charges = businessChargesService.getOne(queryWrapper);

            BusinessCharges businessCharges = new BusinessCharges();
            businessCharges.setId(charges.getId());
            businessCharges.setStatus(feeReceivable.getStatus());
            if (!businessChargesService.updateById(businessCharges)) {
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        }else if(feeReceivable.getType().equals("0")||feeReceivable.getType().equals("2")){
            ContractInfoVo contractInfoVo=new ContractInfoVo();
            contractInfoVo.setContractId(feeReceivable.getGuanLianId());
            QueryWrapper queryWrapper = makeQueryMaps(contractInfoVo);
            ContractInfo contractInfo=contractInfoService.getOne(queryWrapper);
            ContractInfo info=new ContractInfo();
            info.setId(contractInfo.getId());
            if(feeReceivable.getType().equals("0")) {
                info.setOther2(feeReceivable.getStatus());
            }else {
                info.setThMoney(feeReceivable.getMoney());
            }
            if (!contractInfoService.updateById(info)) {
                return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
            }
        }

        if(feeReceivable.getType().equals("3")&&feeReceivable.getStatus().equals("1")){
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("contract_id",feeReceivable.getGuanLianId());
            ContractInfo info=contractInfoService.getOne(wrapper);
            ContractInfo obj=new ContractInfo();
            obj.setId(info.getId());
            BigDecimal total=info.getBushouMoney().add(feeReceivable.getMoney());
            obj.setBushouMoney(total);
            BigDecimal wsMoney=info.getWeishouMoney().subtract(feeReceivable.getMoney());
            BigDecimal ysMoney=info.getAmountReceived().add(feeReceivable.getMoney());
            obj.setWeishouMoney(wsMoney);
            obj.setAmountMoney(ysMoney);
            if(!contractInfoService.updateById(obj)){
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
    @RequiresPermissions("feeReceivable:info")
    public BaseResultData info(Long id) {
        FeeReceivable info = feeReceivableService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

    @GetMapping("/getCountMoney")
    public BaseResultData getCountMoney(FeeReceivableVo vo) throws ParseException {
        String startTime=null;
        String endTime=null;
        if(vo.getEndDate()!=null) {
            startTime=vo.getStartDate();
            endTime=vo.getEndDate();
        }
        vo.setStartDate(null);
        vo.setEndDate(null);
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.eq("status","1");
        if(startTime!=null) {
            queryWrapper.between("create_time", startTime, endTime);
        }
        Map map=new HashMap();
        BigDecimal count=new BigDecimal(0);
        BigDecimal htCount=new BigDecimal(0);
        BigDecimal ywCount=new BigDecimal(0);
        BigDecimal tkCount=new BigDecimal(0);
        BigDecimal jrCount=new BigDecimal(0);
        List<FeeReceivable> list=feeReceivableService.list(queryWrapper);
        for(FeeReceivable receivable:list){

            count=count.add(receivable.getMoney());
            if(receivable.getType().equals("0")){
                htCount=htCount.add(receivable.getMoney());
            }else if(receivable.getType().equals("1")){
                ywCount=ywCount.add(receivable.getMoney());
            }else if(receivable.getType().equals("2")){
                tkCount=tkCount.add(receivable.getMoney());
            }
            SimpleDateFormat sf=new SimpleDateFormat ("yyyy-MM-dd");
            Date amDate=sf.parse((sf.format(new Date())+" 00:00:00"));

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(new Date());
            calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                    23, 59, 59);
            Date pmDate = calendar1.getTime();

            if(receivable.getCreateTime().compareTo(amDate)==1&&
                    receivable.getCreateTime().compareTo(pmDate)==-1){
                jrCount=jrCount.add(receivable.getMoney());
            }
        }
        map.put("zje",count);
        map.put("htje",htCount);
        map.put("ywje",ywCount);
        map.put("tkje",tkCount);
        map.put("jrje",jrCount);
        return BaseResultData.SUCCESS(map);
    }

}