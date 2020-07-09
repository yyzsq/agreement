
package com.gemframework.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gemframework.mapper.DictionaryMapper;
import com.gemframework.model.common.DictionaryMap;
import com.gemframework.model.entity.po.Dictionary;
import com.gemframework.model.enums.DictionaryType;
import com.gemframework.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Title: DictionaryServiceImpl
 * @Date: 2020-04-16 23:39:03
 * @Version: v1.0
 * @Description: 字典表
 * @Author: gem
 * @Email: gemframe@163.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    @Override
    public Dictionary getByKey(String key) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("key_name",key);
        Dictionary dictionary = getOne(queryWrapper);
        if(dictionary != null){
            return dictionary;
        }
        return null;
    }

    @Override
    public String getCfgByKey(String key) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("key_name",key);
        queryWrapper.eq("type",DictionaryType.CONFIG.getCode());
        Dictionary dictionary = getOne(queryWrapper);
        if(dictionary != null){
            return dictionary.getValueStr();
        }
        return null;
    }


    @Override
    public List<DictionaryMap> getMapsByKey(String key) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("key_name",key);
        queryWrapper.eq("type",DictionaryType.OPTIONS.getCode());
        Dictionary dictionary = getOne(queryWrapper);
        if(dictionary != null){
            List<DictionaryMap> list = JSONArray.parseArray(dictionary.getValueStr(), DictionaryMap.class);
            return list;
        }
        return null;
    }

    @Override
    public String getMapValue(String dictionaryKey,String mapKey) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("key_name",dictionaryKey);
        queryWrapper.eq("type",DictionaryType.OPTIONS.getCode());
        Dictionary dictionary = getOne(queryWrapper);
        if(dictionary != null){
            List<DictionaryMap> list = JSONArray.parseArray(dictionary.getValueStr(), DictionaryMap.class);
            for(DictionaryMap map:list){
                if(map.getMapKey().equals(mapKey)){
                    return map.getMapVal();
                }
            }
        }
        return null;
    }
}