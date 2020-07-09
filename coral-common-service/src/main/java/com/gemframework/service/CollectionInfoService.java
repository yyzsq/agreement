
package com.gemframework.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.entity.po.CollectionInfo;
import com.gemframework.model.entity.po.ContractInfo;
import com.gemframework.model.entity.vo.CollectionInfoVo;

import java.text.ParseException;

/**
 * @Title: CollectionInfoService
 * @Date: 2020-05-11 14:19:02
 * @Version: v1.0
 * @Description: 催费报表服务接口
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
public interface CollectionInfoService extends IService<CollectionInfo> {

    public boolean saveColle(ContractInfo co);
    public void saveCollectionInfo(CollectionInfoVo vo) throws ParseException;
}