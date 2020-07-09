
package com.gemframework.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: CopyUtils.java
 * @Package: com.gemframework.util
 * @Date: 2019/11/28 23:12
 * @Version: v1.0
 * @Description: 复制对象工具类

 * @Author: zhangysh
 * @Copyright: Copyright (c) 2019 GemStudio
 * @Company: www.gemframework.com
 */
public class GemBeanUtils {

    /**
     * 单个对象属性复制
     * @param src
     * @param target
     */
    public static void copyProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * 单个对象属性复制，并返回目标对象
     *
     * @param source 复制源
     * @param clazz  目标对象class
     * @param <T>    目标对象类型
     * @param <M>    源对象类型
     * @return 目标对象
     */
    public static <T, M> T copyProperties(M source, Class<T> clazz) {
        if (Objects.isNull(source) || Objects.isNull(clazz))
            throw new IllegalArgumentException();
        T t = null;
        try {
            t = clazz.newInstance();
            BeanUtils.copyProperties(source, t);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }


    /**
     * 列表对象属性复制
     *
     * @param sources 源对象列表
     * @param clazz   目标对象class
     * @param <T>     目标对象类型
     * @param <M>     源对象类型
     * @return 目标对象列表
     */
    public static <T, M> List<T> copyCollections(List<M> sources, Class<T> clazz) {
        if (Objects.isNull(sources) || Objects.isNull(clazz))
            throw new IllegalArgumentException();
        return Optional.of(sources)
                .orElse(new ArrayList<>())
                .stream().map(m -> copyProperties(m, clazz))
                .collect(Collectors.toList());
    }

    public static Map<String,Object> objectToMap(Object obj) {
        Map<String,Object> map = JSONObject.parseObject(JSON.toJSONString(obj));
        return map;
    }

    public static <T> T mapToObject(Map map,Class<T> destinationClass) {
        //map对象
        String json =JSONObject.toJSONString(map);
        return JSONObject.parseObject(json, destinationClass);
    }




    /**
     * 获取为null的属性名
     * @param source
     * @return
     */
    private static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
