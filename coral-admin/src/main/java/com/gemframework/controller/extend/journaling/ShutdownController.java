
package com.gemframework.controller.extend.journaling;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gemframework.annotation.Log;
import com.gemframework.common.utils.GemBeanUtils;
import com.gemframework.constant.GemModules;
import com.gemframework.controller.prekit.BaseController;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.common.PageInfo;
import com.gemframework.model.entity.po.ContractInfo;
import com.gemframework.model.entity.po.CustomerResearch;
import com.gemframework.model.entity.po.User;
import com.gemframework.model.entity.po.UserRoles;
import com.gemframework.model.entity.vo.ContractInfoVo;
import com.gemframework.model.entity.vo.CustomerResearchVo;
import com.gemframework.model.entity.vo.UserRolesVo;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.ContractInfoService;
import com.gemframework.service.CustomerResearchService;
import com.gemframework.service.UserRolesService;
import com.gemframework.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: ContractInfoController
 * @Date: 2020-05-10 18:23:44
 * @Version: v1.0
 * @Description: 合同信息表控制器
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.Extend.PATH_PRE+"/journaling/shutdown")
public class ShutdownController extends BaseController {

    private static final String moduleName = "停止服务";

    @Autowired
    private ContractInfoService contractInfoService;

    @Autowired
    private UserRolesService userRolesService;
    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerResearchService customerResearchService;
    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("shutdown:page")
    public BaseResultData page(PageInfo pageInfo, ContractInfoVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = contractInfoService.page(setOrderPage(pageInfo),queryWrapper);
        List list=new ArrayList();
        for(Object obj:page.getRecords()){
            ContractInfo info=(ContractInfo)obj;
            CustomerResearch customerResearch=customerResearchService.getById(info.getCustomerId());
            ContractInfoVo entity = GemBeanUtils.copyProperties(info, ContractInfoVo.class);
            User user=userService.getById(entity.getSignedBy());
            User user1=userService.getById(entity.getAccounting());
            entity.setSignedName(user.getRealname());
            entity.setAccountingName(user1.getRealname());
            entity.setCustomerName(customerResearch.getCorporateName());
            list.add(entity);
        }
        return BaseResultData.SUCCESS(list,page.getTotal());
    }

    @GetMapping("/countPage")
    public BaseResultData countPage(ContractInfoVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        int count=contractInfoService.count(queryWrapper);
        return BaseResultData.SUCCESS(count);
    }

    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("shutdown:list")
    public BaseResultData list(ContractInfoVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = contractInfoService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }


    /**
     * 获取用户信息ById
     * @return
     */
    @Log(type = OperateType.NORMAL,value = "查看"+moduleName)
    @GetMapping("/info")
    @RequiresPermissions("shutdown:info")
    public BaseResultData info(Long id) {
        ContractInfo info = contractInfoService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

    @GetMapping("/userRoles/list")
    public BaseResultData list(UserRolesVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List<UserRoles> list = userRolesService.list(queryWrapper);
        List<User> userList=new ArrayList<>();
        for(UserRoles roles:list){
            User user=userService.getById(roles.getUserId());
            userList.add(user);
        }
        return BaseResultData.SUCCESS(userList);
    }

    @GetMapping("/customerResearch/list")
    public BaseResultData list(CustomerResearchVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = customerResearchService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }
}