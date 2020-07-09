
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: Session
 * @Date: 2020-04-18 21:24:41
 * @Version: v1.0
 * @Description: 系统会话表实体
 * @Author: gem
 * @Email: gemframe@163.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("gem_sys_session")
@Data
public class Session extends BaseEntityPo {

    /**
     * sessionId
     */
    private String sessionId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户IP
     */
    private String userIp;
    /**
     * 客户端系统 0 win 1 mac os 2 iOS 3 andriod 4 linux 99 other
     */
    private Integer clientOs;
    /**
     * 客户端类型 0 谷歌 1 Firefox 2 IE 3 X5 4 Safari 5 webview 99 other
     */
    private Integer clientType;

}

