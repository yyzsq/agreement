
package com.gemframework.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.ContractInfo;
import com.gemframework.model.entity.po.CustomerResearch;

/**
 * @Title: ContractInfoService
 * @Date: 2020-05-10 18:23:44
 * @Version: v1.0
 * @Description: 合同信息表服务接口
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
public interface ContractInfoService extends IService<ContractInfo> {


    boolean updateById(CustomerResearch entity);
}