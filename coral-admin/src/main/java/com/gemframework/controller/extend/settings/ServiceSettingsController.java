
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
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.gemframework.model.entity.po.ServiceSettings;
import com.gemframework.model.entity.vo.ServiceSettingsVo;
import com.gemframework.service.ServiceSettingsService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: ServiceSettingsController
 * @Date: 2020-05-11 11:14:55
 * @Version: v1.0
 * @Description: 合同信息表控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/settings/serviceSettings")
public class ServiceSettingsController extends BaseController {

    private static final String moduleName = "合同信息表";

    @Autowired
    private ServiceSettingsService serviceSettingsService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("serviceSettings:page")
    public BaseResultData page(PageInfo pageInfo, ServiceSettingsVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = serviceSettingsService.page(setOrderPage(pageInfo),queryWrapper);
        return BaseResultData.SUCCESS(page.getRecords(),page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    /*@RequiresPermissions("serviceSettings:list")*/
    public BaseResultData list(ServiceSettingsVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = serviceSettingsService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("serviceSettings:save")
    public BaseResultData save(@RequestBody ServiceSettingsVo vo) {
        GemValidate(vo, SaveValidator.class);
        ServiceSettings entity = GemBeanUtils.copyProperties(vo, ServiceSettings.class);
        if(!serviceSettingsService.save(entity)){
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
    @RequiresPermissions("serviceSettings:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) serviceSettingsService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    serviceSettingsService.removeByIds(listIds);
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
    @RequiresPermissions("serviceSettings:update")
    public BaseResultData update(@RequestBody ServiceSettingsVo vo) {
        GemValidate(vo, UpdateValidator.class);
        ServiceSettings entity = GemBeanUtils.copyProperties(vo, ServiceSettings.class);
        if(!serviceSettingsService.updateById(entity)){
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
    @RequiresPermissions("serviceSettings:info")
    public BaseResultData info(Long id) {
        ServiceSettings info = serviceSettingsService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

}