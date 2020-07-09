
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
import com.gemframework.model.entity.po.BookingSettings;
import com.gemframework.model.entity.vo.BookingSettingsVo;
import com.gemframework.service.BookingSettingsService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: BookingSettingsController
 * @Date: 2020-05-11 11:42:02
 * @Version: v1.0
 * @Description: 会计星级控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/settings/bookingSettings")
public class BookingSettingsController extends BaseController {

    private static final String moduleName = "会计星级";

    @Autowired
    private BookingSettingsService bookingSettingsService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("bookingSettings:page")
    public BaseResultData page(PageInfo pageInfo, BookingSettingsVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        queryWrapper.notInSql("other1","-1");
        //List list=bookingSettingsService.list();
        Page page = bookingSettingsService.page(setOrderPage(pageInfo),queryWrapper);
        return BaseResultData.SUCCESS(page.getRecords(),page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("bookingSettings:list")
    public BaseResultData list(BookingSettingsVo vo) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("other1",vo.getOther1());
        List list = bookingSettingsService.list(queryWrapper);
       // List list1=bookingSettingsService.list();
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("bookingSettings:save")
    public BaseResultData save(@RequestBody BookingSettingsVo vo) {
        GemValidate(vo, SaveValidator.class);
        BookingSettings entity = GemBeanUtils.copyProperties(vo, BookingSettings.class);
        if(!bookingSettingsService.save(entity)){
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
    @RequiresPermissions("bookingSettings:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) bookingSettingsService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    bookingSettingsService.removeByIds(listIds);
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
    @RequiresPermissions("bookingSettings:update")
    public BaseResultData update(@RequestBody BookingSettingsVo vo) {
        GemValidate(vo, UpdateValidator.class);
        BookingSettings entity = GemBeanUtils.copyProperties(vo, BookingSettings.class);
        if(!bookingSettingsService.updateById(entity)){
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
    @RequiresPermissions("bookingSettings:info")
    public BaseResultData info(Long id) {
        BookingSettings info = bookingSettingsService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

}