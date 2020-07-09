
package com.gemframework.controller.extend.settings;

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
import com.gemframework.model.entity.po.ServiceSettings;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.ServiceSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.gemframework.model.entity.po.BusinessSettings;
import com.gemframework.model.entity.vo.BusinessSettingsVo;
import com.gemframework.service.BusinessSettingsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: BusinessSettingsController
 * @Date: 2020-05-11 12:25:05
 * @Version: v1.0
 * @Description: 业务提成设置控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/settings/businessSettings")
public class BusinessSettingsController extends BaseController {

    private static final String moduleName = "业务提成设置";

    @Autowired
    private BusinessSettingsService businessSettingsService;
    @Autowired
    private ServiceSettingsService serviceSettingsService;
    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("businessSettings:page")
    public BaseResultData page(PageInfo pageInfo, BusinessSettingsVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = businessSettingsService.page(setOrderPage(pageInfo),queryWrapper);
        List list=new ArrayList();
        for(Object obj:page.getRecords()){
            BusinessSettings businessSettings=(BusinessSettings)obj;
            BusinessSettingsVo entity = GemBeanUtils.copyProperties(businessSettings, BusinessSettingsVo.class);
            ServiceSettings service=serviceSettingsService.getById(entity.getServiceId());
            if(service!=null)
            entity.setServiceName(service.getServiceName());
            list.add(entity);
        }
        return BaseResultData.SUCCESS(list,page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("businessSettings:list")
    public BaseResultData list(BusinessSettingsVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = businessSettingsService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("businessSettings:save")
    public BaseResultData save(@RequestBody BusinessSettingsVo vo) {
        GemValidate(vo, SaveValidator.class);
        BusinessSettings entity = GemBeanUtils.copyProperties(vo, BusinessSettings.class);
        if(!businessSettingsService.save(entity)){
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
    @RequiresPermissions("businessSettings:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) businessSettingsService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    businessSettingsService.removeByIds(listIds);
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
    @RequiresPermissions("businessSettings:update")
    public BaseResultData update(@RequestBody BusinessSettingsVo vo) {
        GemValidate(vo, UpdateValidator.class);
        BusinessSettings entity = GemBeanUtils.copyProperties(vo, BusinessSettings.class);
        if(!businessSettingsService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }


    /**
     * 获取用户信息ById
     * @return
     */
    @Log(type = OperateType.NORMAL,value = "查看"+moduleName)
    @GetMapping("/info")
    @RequiresPermissions("businessSettings:info")
    public BaseResultData info(Long id) {
        BusinessSettings info = businessSettingsService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

}