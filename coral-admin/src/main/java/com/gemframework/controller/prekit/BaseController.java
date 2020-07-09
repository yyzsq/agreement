
package com.gemframework.controller.prekit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gemframework.common.config.GemSystemProperties;
import com.gemframework.common.exception.GemException;
import com.gemframework.common.utils.GemBeanUtils;
import com.gemframework.common.utils.GemRedisUtils;
import com.gemframework.common.utils.GemStringUtils;
import com.gemframework.model.common.BaseEntityVo;
import com.gemframework.model.common.PageInfo;
import com.gemframework.model.common.ZtreeEntity;
import com.gemframework.model.entity.po.User;
import com.gemframework.model.entity.vo.ContractInfoVo;
import com.gemframework.model.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.gemframework.common.constant.GemRedisKeys.Auth.USER_ROLES;
import static com.gemframework.common.constant.GemSessionKeys.CURRENT_USER_KEY;

@Slf4j
public class BaseController {

    @Autowired
    GemRedisUtils gemRedisUtils;

    @Autowired
    GemSystemProperties gemSystemProperties;

    @NotNull
    public static Page setOrderPage(PageInfo pageInfo) {
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("sort_number").setAsc(true);
        orderItem.setColumn("update_time").setAsc(false);
        if(pageInfo!=null && pageInfo.getOrder()!=null){
            orderItem.setColumn(pageInfo.getSort()).setAsc(pageInfo.getOrder().getCode() == 0);
        }

        List orders = new ArrayList();
        orders.add(orderItem);
        Page page = new Page();
        page.setOrders(orders);
        page.setSize(pageInfo.getLimit());
        page.setCurrent(pageInfo.getPage());
        return page;
    }

    public static QueryWrapper setSort() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("sort_number");
        return queryWrapper;
    }

    public static QueryWrapper makeQueryMaps(BaseEntityVo vo) {
        QueryWrapper queryWrapper = setSort();
        queryWrapper.eq("deleted",0);
        Map<String,Object> map = GemBeanUtils.objectToMap(vo);
        for (Map.Entry<String, Object> entry : map.entrySet()) {

            String fieldName = GemStringUtils.humpToLine(entry.getKey());
            Object paramVal = entry.getValue();
            log.debug("key= " + fieldName + " and value= "+paramVal);
            if(entry.getKey().equalsIgnoreCase("startDate")){
                queryWrapper.ge(paramVal != null && StringUtils.isNotBlank(String.valueOf(paramVal)),"update_time",paramVal);
            }else if(entry.getKey().equalsIgnoreCase("endDate")){
                queryWrapper.le(paramVal != null && StringUtils.isNotBlank(String.valueOf(paramVal)),"update_time",paramVal);
            }else{
                queryWrapper.like(paramVal != null && StringUtils.isNotBlank(String.valueOf(paramVal)),fieldName,paramVal);
            }
        }
        return queryWrapper;
    }


    /**
     * 将list格式是权限数据，转化成tree格式的权限数据。
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public static List<ZtreeEntity> toTree(List<ZtreeEntity> treeNodes) {
        List<ZtreeEntity> trees = new ArrayList<ZtreeEntity>();
        for (ZtreeEntity treeNode : treeNodes) {
            if (-1 == (treeNode.getPid())) {
                trees.add(treeNode);
            }
            for (ZtreeEntity it : treeNodes) {
                if (it.getPid().toString().equals(treeNode.getId().toString())) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<ZtreeEntity>());
                    }
                    treeNode.getChildren().add(it);
                }
            }
        }
        return trees;
    }


    public static List<ZtreeEntity> initRootTree() {
        List<ZtreeEntity> ztreeEntities = new ArrayList<>();
        ZtreeEntity ztreeEntity = ZtreeEntity.builder()
                .id(0L)
                .pid(-1L)
                .name("顶层目录|全选/全消")
                .title("顶层目录|全选/全消")
                .level(0)
                .open(true)
                .nocheck(true)
                .build();
        ztreeEntities.add(ztreeEntity);

        return ztreeEntities;
    }

    public static void GemValidate(Object object, Class<?>... groups){
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint = (ConstraintViolation<Object>)constraintViolations.iterator().next();
            throw new GemException(ErrorCode.PARAM_EXCEPTION.getCode(),constraint.getMessage());
        }
    }

    protected User getUser() {
        return (User) SecurityUtils.getSubject().getSession().getAttribute(CURRENT_USER_KEY);
    }

    protected String getUsername() {
        return (String)SecurityUtils.getSubject().getPrincipal();
    }

    protected Set<String> getRolesFlag() {
        String userName = (String)SecurityUtils.getSubject().getPrincipal();
        //如果Redis集群开启则从redis中取
        if(gemSystemProperties.isCluster()){
            return (Set<String>) gemRedisUtils.get(userName + "_" + USER_ROLES);
        }else{
            return (Set<String>) SecurityUtils.getSubject().getSession().getAttribute(userName + "_" + USER_ROLES);
        }
    }
}