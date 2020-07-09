
package com.gemframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.MemberMapper;
import com.gemframework.model.entity.po.Dept;
import com.gemframework.model.entity.po.Member;
import com.gemframework.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Override
    public boolean exits(Member entity) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",entity.getAccount());
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