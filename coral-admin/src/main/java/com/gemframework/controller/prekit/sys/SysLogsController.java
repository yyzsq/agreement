
package com.gemframework.controller.prekit.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gemframework.annotation.Log;
import com.gemframework.common.utils.GemBeanUtils;
import com.gemframework.constant.GemModules;
import com.gemframework.controller.prekit.BaseController;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.common.PageInfo;
import com.gemframework.model.common.validator.*;
import com.gemframework.model.entity.po.SysLogs;
import com.gemframework.model.entity.vo.SysLogsVo;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.SysLogsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(GemModules.PreKit.PATH_SYSTEM+"/sysLogs")
public class SysLogsController extends BaseController {

    private static final String moduleName = "系统操作日志";

    @Autowired
    private SysLogsService sysLogsService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("sysLogs:page")
    public BaseResultData page(PageInfo pageInfo, SysLogsVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = sysLogsService.page(setOrderPage(pageInfo),queryWrapper);
        return BaseResultData.SUCCESS(page.getRecords(),page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("sysLogs:list")
    public BaseResultData list(SysLogsVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = sysLogsService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("sysLogs:save")
    public BaseResultData save(@RequestBody SysLogsVo vo) {
        GemValidate(vo, StatusValidator.class);
        SysLogs entity = GemBeanUtils.copyProperties(vo, SysLogs.class);
        if(!sysLogsService.save(entity)){
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
    @RequiresPermissions("sysLogs:update")
    public BaseResultData update(@RequestBody SysLogsVo vo) {
        GemValidate(vo, UpdateValidator.class);
        SysLogs entity = GemBeanUtils.copyProperties(vo, SysLogs.class);
        if(!sysLogsService.updateById(entity)){
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
    @RequiresPermissions("sysLogs:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) sysLogsService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                sysLogsService.removeByIds(listIds);
            }
        }
        return BaseResultData.SUCCESS();
    }


}