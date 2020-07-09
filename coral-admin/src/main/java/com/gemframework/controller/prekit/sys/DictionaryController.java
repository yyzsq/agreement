
package com.gemframework.controller.prekit.sys;

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
import com.gemframework.model.entity.po.Dictionary;
import com.gemframework.model.entity.vo.DictionaryVo;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: DictionaryController
 * @Date: 2020-04-16 23:50:56
 * @Version: v1.0
 * @Description: 字典表控制器
 * @Author: gem
 * @Email: gemframe@163.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.PreKit.PATH_SYSTEM+"/dictionary")
public class DictionaryController extends BaseController {

    private static final String moduleName = "字典表";

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("dictionary:page")
    public BaseResultData page(PageInfo pageInfo, DictionaryVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = dictionaryService.page(setOrderPage(pageInfo),queryWrapper);
        return BaseResultData.SUCCESS(page.getRecords(),page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("dictionary:list")
    public BaseResultData list(DictionaryVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = dictionaryService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("dictionary:save")
    public BaseResultData save(@RequestBody DictionaryVo vo) {
        GemValidate(vo, SaveValidator.class);
        Dictionary entity = GemBeanUtils.copyProperties(vo, Dictionary.class);
        if(!dictionaryService.save(entity)){
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
    @RequiresPermissions("dictionary:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) dictionaryService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    dictionaryService.removeByIds(listIds);
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
    @RequiresPermissions("dictionary:update")
    public BaseResultData update(@RequestBody DictionaryVo vo) {
        GemValidate(vo, UpdateValidator.class);
        Dictionary entity = GemBeanUtils.copyProperties(vo, Dictionary.class);
        if(!dictionaryService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }


    /**
     * 获取信息ById
     * @return
     */
    @Log(type = OperateType.NORMAL,value = "查看"+moduleName)
    @GetMapping("/info")
    @RequiresPermissions("dictionary:info")
    public BaseResultData info(Long id) {
        Dictionary info = dictionaryService.getById(id);
        return BaseResultData.SUCCESS(info);
    }



    @GetMapping("/cache")
    @RequiresPermissions("dictionary:cache")
    public BaseResultData syncCache(Long id) {
        Dictionary info = dictionaryService.getById(id);
        //TODO: 将系统字典，常量配置同步到缓存中，提高系统性能
        return BaseResultData.SUCCESS(info);
    }

}