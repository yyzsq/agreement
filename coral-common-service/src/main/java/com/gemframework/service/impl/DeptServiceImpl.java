
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.DeptMapper;
import com.gemframework.model.entity.po.Dept;
import com.gemframework.service.DeptService;
import org.springframework.stereotype.Service;

@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public boolean exits(Dept entity) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",entity.getName());
        //编辑
        if(entity.getId() != null && entity.getId() !=0){
            queryWrapper.and(wrapper -> wrapper.ne("id",entity.getId()));
        }
        if(count(queryWrapper)>0){
            return true;
        }
        return false;
    }
}