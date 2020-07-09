
package com.gemframework.controller.prekit.rbac;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gemframework.annotation.Log;
import com.gemframework.common.utils.GemBeanUtils;
import com.gemframework.constant.GemModules;
import com.gemframework.controller.prekit.BaseController;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.common.PageInfo;
import com.gemframework.model.common.ZtreeEntity;
import com.gemframework.model.common.validator.StatusValidator;
import com.gemframework.model.common.validator.UpdateValidator;
import com.gemframework.model.entity.po.Dept;
import com.gemframework.model.entity.vo.DeptVo;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.DeptService;
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
@RequestMapping(GemModules.PreKit.PATH_RBAC+"/dept")
public class DeptController extends BaseController {

    private static final String moduleName = "机构（部门）信息";

    @Autowired
    private DeptService deptService;


    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("dept:page")
    public BaseResultData page(PageInfo pageInfo,DeptVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = deptService.page(setOrderPage(pageInfo),queryWrapper);
        return BaseResultData.SUCCESS(page.getRecords(),page.getTotal());
    }

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("dept:list")
    public BaseResultData list(DeptVo vo) {
        Dept dept = new Dept();
        dept.setId(0L);
        dept.setPid(-1L);
        dept.setName("金管家总部");
        dept.setFullname("金管家总部");
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = deptService.list(queryWrapper);
        list.add(dept);
        return BaseResultData.SUCCESS(list);
    }


    /***
     * 获取部门树
     * @return
     */
    @GetMapping("/tree")
    @RequiresPermissions("dept:tree")
    public BaseResultData tree(){
        QueryWrapper queryWrapper = setSort();
        List<Dept> list = deptService.list(queryWrapper);
        List<ZtreeEntity> ztreeEntities = initRootTree();
        for(Dept entity:list){
            ZtreeEntity ztreeEntity = ZtreeEntity.builder()
                    .id(entity.getId())
                    .pid(entity.getPid())
                    .name(entity.getName())
                    .title(entity.getName())
                    .level(entity.getLevel())
                    .open(true)
                    .nocheck(false)
                    .build();
            ztreeEntities.add(ztreeEntity);
        }
        return BaseResultData.SUCCESS(toTree(ztreeEntities));
    }

    /**
     * 添加或编辑
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("dept:save")
    public BaseResultData save(@RequestBody DeptVo vo) {
        GemValidate(vo, StatusValidator.class);
        Dept entity = GemBeanUtils.copyProperties(vo, Dept.class);
        if(deptService.exits(entity)){
            return BaseResultData.ERROR(ErrorCode.DEPT_EXIST);
        }
        if(!deptService.save(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }

    /**
     * 添加或编辑
     * @return
     */
    @Log(type = OperateType.ALTER,value = "编辑"+moduleName)
    @PostMapping("/update")
    @RequiresPermissions("dept:update")
    public BaseResultData update(@RequestBody DeptVo vo) {
        GemValidate(vo, UpdateValidator.class);
        Dept entity = GemBeanUtils.copyProperties(vo, Dept.class);
        if(deptService.exits(entity)){
            return BaseResultData.ERROR(ErrorCode.DEPT_EXIST);
        }
        if(!deptService.updateById(entity)){
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
    @RequiresPermissions("dept:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) deptService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                deptService.removeByIds(listIds);
            }
        }
        return BaseResultData.SUCCESS();
    }

}