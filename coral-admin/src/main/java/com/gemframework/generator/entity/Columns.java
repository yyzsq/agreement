
package com.gemframework.generator.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @Title: ColumnEntity
 * @Package: com.gemframework.generator
 * @Date: 2020-04-14 18:59:06
 * @Version: v1.0
 * @Description: 数据表的列
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
@Builder
public class Columns {
    private String columnName;
    private String dataType;
    private String comments;

    //属性名称(第一个字母大写)，如：user_name => UserName
    private String attrNameUp;
    //属性名称(第一个字母小写)，如：user_name => userName
    private String attrNameLow;
    //属性类型
    private String attrType;
    //auto_increment
    private String extra;
    //是否允许为空
    private String isNull;
}
