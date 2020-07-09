
package com.gemframework.controller.extend.ticheng;

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
import com.gemframework.model.entity.po.TichengContract;
import com.gemframework.model.entity.vo.TichengContractVo;
import com.gemframework.service.TichengContractService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: TichengContractController
 * @Date: 2020-06-22 11:29:17
 * @Version: v1.0
 * @Description: 提成关联合同编号控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/ticheng/tichengContract")
public class TichengContractController extends BaseController {

    private static final String moduleName = "提成关联合同编号";

    @Autowired
    private TichengContractService tichengContractService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("tichengContract:page")
    public BaseResultData page(PageInfo pageInfo, TichengContractVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = tichengContractService.page(setOrderPage(pageInfo),queryWrapper);
        return BaseResultData.SUCCESS(page.getRecords(),page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("tichengContract:list")
    public BaseResultData list(TichengContractVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = tichengContractService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("tichengContract:save")
    public BaseResultData save(@RequestBody TichengContractVo vo) {
        GemValidate(vo, SaveValidator.class);
        TichengContract entity = GemBeanUtils.copyProperties(vo, TichengContract.class);
        if(!tichengContractService.save(entity)){
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
    @RequiresPermissions("tichengContract:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) tichengContractService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    tichengContractService.removeByIds(listIds);
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
    @RequiresPermissions("tichengContract:update")
    public BaseResultData update(@RequestBody TichengContractVo vo) {
        GemValidate(vo, UpdateValidator.class);
        TichengContract entity = GemBeanUtils.copyProperties(vo, TichengContract.class);
        if(!tichengContractService.updateById(entity)){
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
    @RequiresPermissions("tichengContract:info")
    public BaseResultData info(Long id) {
        TichengContract info = tichengContractService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

}