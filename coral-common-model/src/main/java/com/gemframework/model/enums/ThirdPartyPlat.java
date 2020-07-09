
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
public enum ThirdPartyPlat {

    //第三方平台 1 gitee 2github 3 微信 4 QQ 5 微博 6支付宝
    GITEE(1,"GITEE"),
    GITHUB(2,"GITHUB"),
    WECHAT(3,"微信"),
    QQ(4,"QQ"),
    WEIBO(5,"微博"),
    ALIPAY(6,"支付宝"),
    TAOBAO(7,"淘宝"),
    OTHER(99,"其他"),
    ;


    private Integer code;
    private String msg;

    ThirdPartyPlat(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
