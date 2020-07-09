
package com.gemframework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gemframework.model.entity.po.Dictionary;
import com.gemframework.model.entity.po.Generator;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Title: GeneratorMapper
 * @Package: com.gemframework.mapper
 * @Date: 2020-03-09 15:31:33
 * @Version: v1.0
 * @Description: GeneratorMapper
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@Repository
public interface GeneratorMapper extends BaseMapper<Generator> {

    List<Map<String, Object>> queryList(String tableName);

    Map<String, String> queryTable(String tableName);

    List<Map<String, String>> queryColumns(String tableName);

    Map<String, String> queryGlobalCfg();
}