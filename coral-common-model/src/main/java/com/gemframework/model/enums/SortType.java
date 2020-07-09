
package com.gemframework.model.enums;

import lombok.Getter;

/**
 * @Title: SortType.java
 * @Package: com.gemframework.enum
 * @Date: 2019/11/27 22:28
 * @Version: v1.0
 * @Description: 枚举类

 * @Author: zhangysh
 * @Copyright: Copyright (c) 2019 GemStudio
 * @Company: www.gemframework.com
 */
@Getter
public enum SortType {
    asc(0,"asc"),
    desc(1,"desc"),
    ;


    private Integer code;
    private String msg;

    SortType(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
