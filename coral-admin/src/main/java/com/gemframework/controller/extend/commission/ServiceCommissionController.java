
package com.gemframework.controller.extend.commission;

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
import com.gemframework.model.entity.po.User;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import com.gemframework.model.entity.po.ServiceCommission;
import com.gemframework.model.entity.vo.ServiceCommissionVo;
import com.gemframework.service.ServiceCommissionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: ServiceCommissionController
 * @Date: 2020-05-28 11:46:31
 * @Version: v1.0
 * @Description: 业务提成控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/commission/serviceCommission")
public class ServiceCommissionController extends BaseController {

    private static final String moduleName = "业务提成";

    @Autowired
    private ServiceCommissionService serviceCommissionService;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("serviceCommission:page")
    public BaseResultData page(PageInfo pageInfo, ServiceCommissionVo vo) {
        Long userIds=null;
        if(StringUtils.isNotBlank(vo.getUserId())){
            userIds=Long.parseLong(vo.getUserId());
            vo.setUserId(null);
        }

        QueryWrapper queryWrapper = makeQueryMaps(vo);
        if(userIds!=null) {
            queryWrapper.eq("user_id", userIds);
        }
        Page page = serviceCommissionService.page(setOrderPage(pageInfo),queryWrapper);
        List list=new ArrayList();
        for(Object obj:page.getRecords()){
            ServiceCommissionVo entity = GemBeanUtils.copyProperties((ServiceCommission)obj, ServiceCommissionVo.class);
            User user=userService.getById(entity.getUserId());
            entity.setUserName(user.getRealname());
            list.add(entity);
        }
        return BaseResultData.SUCCESS(list,page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("serviceCommission:list")
    public BaseResultData list(ServiceCommissionVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = serviceCommissionService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("serviceCommission:save")
    public BaseResultData save(@RequestBody ServiceCommissionVo vo) {
        GemValidate(vo, SaveValidator.class);
        ServiceCommission entity = GemBeanUtils.copyProperties(vo, ServiceCommission.class);
        if(!serviceCommissionService.save(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }

    @Log(type = OperateType.ALTER,value = "获取数据"+moduleName)
    @PostMapping("/scsj")
    public BaseResultData scsj() {
        serviceCommissionService.testAsyncJob();
        return BaseResultData.SUCCESS("数据生成成功！");
    }


    /**
     * 删除 & 批量刪除
     * @return
     */
    @Log(type = OperateType.ALTER,value = "删除"+moduleName)
    @PostMapping("/delete")
    @RequiresPermissions("serviceCommission:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) serviceCommissionService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    serviceCommissionService.removeByIds(listIds);
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
    @RequiresPermissions("serviceCommission:update")
    public BaseResultData update(@RequestBody ServiceCommissionVo vo) {
        GemValidate(vo, UpdateValidator.class);
        ServiceCommission entity = GemBeanUtils.copyProperties(vo, ServiceCommission.class);
        if(!serviceCommissionService.updateById(entity)){
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
    @RequiresPermissions("serviceCommission:info")
    public BaseResultData info(Long id) {
        ServiceCommission info = serviceCommissionService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

}