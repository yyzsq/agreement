
package com.gemframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.Member;

public interface MemberService extends IService<Member> {

    boolean exits(Member entity);
}