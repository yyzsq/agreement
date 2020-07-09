
package com.gemframework.model.enums;

import lombok.Getter;

/**
 * @Title: ResultCode.java
 * @Package: com.gemframework.enum
 * @Date: 2019/11/27 22:28
 * @Version: v1.0
 * @Description: 枚举类

 * @Author: zhangysh
 * @Copyright: Copyright (c) 2019 GemStudio
 * @Company: www.gemframework.com
 */
@Getter
public enum DictionaryType {
     //1 配置 k-v 2 字典 k-map options
    CONFIG(1,"配置"),
    OPTIONS(2,"选项"),
    OTHER(3,"其他"),
    ;


    private Integer code;
    private String msg;

    DictionaryType(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
