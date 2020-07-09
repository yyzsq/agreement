
package com.gemframework.model.enums;

import lombok.Getter;

/**
 * @Title: CodeType.java
 * @Package: com.gemframework.enum
 * @Date: 2019/11/27 22:28
 * @Version: v1.0
 * @Description: 枚举类

 * @Author: zhangysh
 * @Copyright: Copyright (c) 2019 GemStudio
 * @Company: www.gemframework.com
 */
@Getter
public enum ChannelType {

    //注册渠道 1 手动添加 2 自助注册 3 oauth
    ADD(1,"手动添加"),
    ONESELF(2,"自助注册"),
    OAUTH(3,"oauth"),
    OTHER(99,"其他"),
    ;


    private Integer code;
    private String msg;

    ChannelType(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
