
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
public enum StatusEnum {
    NORMAL(0,"正常"),
    FORBIDDEN(1,"禁用"),
    ;


    private Integer code;
    private String msg;

    StatusEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
