
package com.gemframework.controller.extend.wechat;

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
import com.gemframework.model.entity.po.WechatJilu;
import com.gemframework.model.entity.vo.WechatJiluVo;
import com.gemframework.service.WechatJiluService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: WechatJiluController
 * @Date: 2020-05-31 12:57:32
 * @Version: v1.0
 * @Description: 微信通知记录控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/wechat/wechatJilu")
public class WechatJiluController extends BaseController {

    private static final String moduleName = "微信通知记录";

    @Autowired
    private WechatJiluService wechatJiluService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("wechatJilu:page")
    public BaseResultData page(PageInfo pageInfo, WechatJiluVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = wechatJiluService.page(setOrderPage(pageInfo),queryWrapper);
        return BaseResultData.SUCCESS(page.getRecords(),page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("wechatJilu:list")
    public BaseResultData list(WechatJiluVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = wechatJiluService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("wechatJilu:save")
    public BaseResultData save(@RequestBody WechatJiluVo vo) {
        GemValidate(vo, SaveValidator.class);
        WechatJilu entity = GemBeanUtils.copyProperties(vo, WechatJilu.class);
        if(!wechatJiluService.save(entity)){
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
    @RequiresPermissions("wechatJilu:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) wechatJiluService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    wechatJiluService.removeByIds(listIds);
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
    @RequiresPermissions("wechatJilu:update")
    public BaseResultData update(@RequestBody WechatJiluVo vo) {
        GemValidate(vo, UpdateValidator.class);
        WechatJilu entity = GemBeanUtils.copyProperties(vo, WechatJilu.class);
        if(!wechatJiluService.updateById(entity)){
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
    @RequiresPermissions("wechatJilu:info")
    public BaseResultData info(Long id) {
        WechatJilu info = wechatJiluService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

}