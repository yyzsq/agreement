
package com.gemframework.controller.extend.journaling;

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
import com.gemframework.model.entity.po.CustomerResearch;
import com.gemframework.model.entity.po.User;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.CustomerResearchService;
import com.gemframework.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import com.gemframework.model.entity.po.CollectionInfo;
import com.gemframework.model.entity.vo.CollectionInfoVo;
import com.gemframework.service.CollectionInfoService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: CollectionInfoController
 * @Date: 2020-05-11 14:19:02
 * @Version: v1.0
 * @Description: 催费报表控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/journaling/collectionInfo")
public class CollectionInfoController extends BaseController {

    private static final String moduleName = "催费报表";

    @Autowired
    private CollectionInfoService collectionInfoService;
    @Autowired
    private CustomerResearchService customerResearchService;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("collectionInfo:page")
    public BaseResultData page(PageInfo pageInfo, CollectionInfoVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = collectionInfoService.page(setOrderPage(pageInfo),queryWrapper);
        List list=new ArrayList();
        for(Object obj:page.getRecords()){
            CollectionInfo collectionInfo=(CollectionInfo)obj;
            CollectionInfoVo entity = GemBeanUtils.copyProperties(collectionInfo, CollectionInfoVo.class);
            if(entity.getUserId()!=null){
                User user=userService.getById(entity.getUserId());
                entity.setUserName(user.getRealname());
            }
            list.add(entity);
        }
        return BaseResultData.SUCCESS(list,page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("collectionInfo:list")
    public BaseResultData list(CollectionInfoVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = collectionInfoService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("collectionInfo:save")
    public BaseResultData save(@RequestBody CollectionInfoVo vo) {
        GemValidate(vo, SaveValidator.class);
        CollectionInfo entity = GemBeanUtils.copyProperties(vo, CollectionInfo.class);
        if(!collectionInfoService.save(entity)){
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
    @RequiresPermissions("collectionInfo:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) collectionInfoService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                    collectionInfoService.removeByIds(listIds);
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
    @RequiresPermissions("collectionInfo:update")
    public BaseResultData update(@RequestBody CollectionInfoVo vo) {
        GemValidate(vo, UpdateValidator.class);
        CollectionInfo entity = GemBeanUtils.copyProperties(vo, CollectionInfo.class);
        if(!collectionInfoService.updateById(entity)){
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
    @RequiresPermissions("collectionInfo:info")
    public BaseResultData info(Long id) {
        CollectionInfo info = collectionInfoService.getById(id);
        return BaseResultData.SUCCESS(info);
    }


    /**
     * 生成数据
     * @return
     */
    @Log(type = OperateType.NORMAL,value = "生成数据"+moduleName)
    @GetMapping("/shencheng")
    public BaseResultData shencheng(CollectionInfoVo vo) {
        try {
            collectionInfoService.saveCollectionInfo(vo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return BaseResultData.SUCCESS();
    }
}