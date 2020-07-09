
package com.gemframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.Dept;

public interface DeptService extends IService<Dept> {

    boolean exits(Dept entity);
}