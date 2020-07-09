
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: BusinessSettings
 * @Date: 2020-05-11 12:25:05
 * @Version: v1.0
 * @Description: 业务提成设置实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_business_settings")
@Data
public class BusinessSettings extends BaseEntityPo {

    /**
     * 业务名称
     */
    private String businessName;
    private String type;
    /**
     * 提成比例/金额
     */
    private String commission;

    private String serviceId;

    private String clerk;
    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

}

