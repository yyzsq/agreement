
package com.gemframework.controller.prekit.demo;

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
import com.gemframework.model.entity.po.Demo;
import com.gemframework.model.entity.po.User;
import com.gemframework.model.entity.vo.DemoVo;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: DemoController
 * @Package: com.gemframework.controller.prekit.demo
 * @Date: 2020-04-16 13:36:13
 * @Version: v1.0
 * @Description: 这里写描述
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.PreKit.PATH_DEMO+"/demo")
public class DemoController extends BaseController {

    private static final String moduleName = "示例模块";

    @Autowired
    private DemoService demoService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("demo:page")
    public BaseResultData page(PageInfo pageInfo, DemoVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = demoService.page(setOrderPage(pageInfo),queryWrapper);
        return BaseResultData.SUCCESS(page.getRecords(),page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("demo:list")
    public BaseResultData list(DemoVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = demoService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("demo:save")
    public BaseResultData save(@RequestBody DemoVo vo) {
        GemValidate(vo, SaveValidator.class);
        Demo entity = GemBeanUtils.copyProperties(vo, Demo.class);
        if(!demoService.save(entity)){
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
    @RequiresPermissions("demo:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) demoService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                demoService.removeByIds(listIds);
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
    @RequiresPermissions("demo:update")
    public BaseResultData update(@RequestBody DemoVo vo) {
        GemValidate(vo, UpdateValidator.class);
        Demo entity = GemBeanUtils.copyProperties(vo, Demo.class);
        if(!demoService.updateById(entity)){
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
    @RequiresPermissions("demo:info")
    public BaseResultData info(Long id) {
        Demo info = demoService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

}