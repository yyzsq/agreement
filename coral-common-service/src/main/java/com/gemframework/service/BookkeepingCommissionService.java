
package com.gemframework.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.BookkeepingCommission;

/**
 * @Title: BookkeepingCommissionService
 * @Date: 2020-05-15 16:19:14
 * @Version: v1.0
 * @Description: 培训报名记录表服务接口
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
public interface BookkeepingCommissionService extends IService<BookkeepingCommission> {

    public void testAsyncJob();
}