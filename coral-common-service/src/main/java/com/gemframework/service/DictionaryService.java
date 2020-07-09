
package com.gemframework.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gemframework.model.common.DictionaryMap;
import com.gemframework.model.entity.po.Dictionary;

import java.util.List;

/**
 * @Title: DictionaryService
 * @Date: 2020-04-16 23:39:03
 * @Version: v1.0
 * @Description: 字典表
 * @Author: gem
 * @Email: gemframe@163.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
public interface DictionaryService extends IService<Dictionary> {

    Dictionary getByKey(String key);

    String getCfgByKey(String key);

    List<DictionaryMap> getMapsByKey(String key);

    String getMapValue(String dictionaryKey,String mapKey);
}