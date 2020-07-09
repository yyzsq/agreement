
package com.gemframework.model.entity.vo;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;

/**
 * @Title: WechatNoticeVo
 * @Date: 2020-05-13 11:57:21
 * @Version: v1.0
 * @Description: 微信通知绑定VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class WechatNoticeVo extends BaseEntityVo {

    /**
     * 公司编号
     */
    private String customerId;

    private String customerName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 名称
     */
    private String name;

    private String operId;
    private String accessToken;
    /**
     * 其他1
     */
    private String other1;
    /**
     * 其他2
     */
    private String other2;

    private String status;

}
