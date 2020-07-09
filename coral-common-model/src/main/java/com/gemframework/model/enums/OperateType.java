
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
public enum OperateType {
    NORMAL(0,"常规操作"),
    ALTER(1,"修改操作"),
    LOGIN(2,"登录/登出"),
    OTHER(9,"其他"),
    ;


    private Integer code;
    private String msg;

    OperateType(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
