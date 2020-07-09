
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
import com.gemframework.model.entity.po.BookkeepingCommission;
import com.gemframework.model.entity.vo.BookkeepingCommissionVo;
import com.gemframework.service.BookkeepingCommissionService;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: BookkeepingCommissionController
 * @Date: 2020-05-15 16:19:14
 * @Version: v1.0
 * @Description: 培训报名记录表控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/commission/commission")
public class BookkeepingCommissionController extends BaseController {

    private static final String moduleName = "记账提成";

    @Autowired
    private BookkeepingCommissionService bookkeepingCommissionService;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("commission:page")
    public BaseResultData page(PageInfo pageInfo, BookkeepingCommissionVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = bookkeepingCommissionService.page(setOrderPage(pageInfo),queryWrapper);
        List list=new ArrayList();
        for(Object obj:page.getRecords()){
            BookkeepingCommission bc=(BookkeepingCommission)obj;
            BookkeepingCommissionVo entity = GemBeanUtils.copyProperties(bc, BookkeepingCommissionVo.class);
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
    @RequiresPermissions("commission:list")
    public BaseResultData list(BookkeepingCommissionVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = bookkeepingCommissionService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("commission:save")
    public BaseResultData save(@RequestBody BookkeepingCommissionVo vo) {
        GemValidate(vo, SaveValidator.class);
        BookkeepingCommission entity = GemBeanUtils.copyProperties(vo, BookkeepingCommission.class);
        if(!bookkeepingCommissionService.save(entity)){
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
    @RequiresPermissions("commission:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) bookkeepingCommissionService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    bookkeepingCommissionService.removeByIds(listIds);
            }
        }
        return BaseResultData.SUCCESS();
    }

    @Log(type = OperateType.ALTER,value = "获取数据"+moduleName)
    @PostMapping("/scsj")
    public BaseResultData scsj() {
        bookkeepingCommissionService.testAsyncJob();
        return BaseResultData.SUCCESS("数据生成成功！");
    }

    /**
     * 编辑
     * @return
     */
    @Log(type = OperateType.ALTER,value = "编辑"+moduleName)
    @PostMapping("/update")
    @RequiresPermissions("commission:update")
    public BaseResultData update(@RequestBody BookkeepingCommissionVo vo) {
        GemValidate(vo, UpdateValidator.class);
        BookkeepingCommission entity = GemBeanUtils.copyProperties(vo, BookkeepingCommission.class);
        if(!bookkeepingCommissionService.updateById(entity)){
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
    @RequiresPermissions("commission:info")
    public BaseResultData info(Long id) {
        BookkeepingCommission info = bookkeepingCommissionService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

}