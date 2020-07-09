
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.RoleRightsMapper;
import com.gemframework.model.entity.po.RoleRights;
import com.gemframework.model.entity.vo.RoleRightsVo;
import com.gemframework.service.RoleRightsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleRightServiceImpl extends ServiceImpl<RoleRightsMapper, RoleRights> implements RoleRightsService {

    @Autowired
    RoleRightsMapper roleRightsMapper;

    @Override
    @Transactional
    public boolean save(RoleRightsVo vo) {
        //先删除
        deleteByRoleId(vo.getRoleId());

        //重新保存
        if(StringUtils.isNotBlank(vo.getRightIds())){
            List<RoleRights> list = new ArrayList<>();
            List<Long> rightIds = Arrays.asList(vo.getRightIds().split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            for(Long rightId : rightIds){
                RoleRights entity = new RoleRights();
                entity.setRoleId(vo.getRoleId());
                entity.setRightId(rightId);
                list.add(entity);
            }
            this.saveBatch(list);
        }
        return true;
    }

    @Override
    public boolean deleteByRoleId(Long roleId) {
        List roleRightIds = roleRightsMapper.findIdsByRoleId(roleId);
        if(roleRightIds!=null && !roleRightIds.isEmpty()){
            roleRightsMapper.deleteBatchIds(roleRightIds);
        }
        return true;
    }

}