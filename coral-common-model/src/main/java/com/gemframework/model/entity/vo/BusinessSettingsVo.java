
package com.gemframework.model.entity.vo;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;

/**
 * @Title: BusinessSettingsVo
 * @Date: 2020-05-11 12:25:05
 * @Version: v1.0
 * @Description: 业务提成设置VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class BusinessSettingsVo extends BaseEntityVo {

    /**
     * 业务名称
     */
    private String businessName;

    private String type;

    private String serviceId;

    private String serviceName;
    /**
     * 提成比例/金额
     */
    private String commission;

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
