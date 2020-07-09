
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: WechatNotice
 * @Date: 2020-05-13 11:57:21
 * @Version: v1.0
 * @Description: 微信通知绑定实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_wechat_notice")
@Data
public class WechatNotice extends BaseEntityPo {

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

