
package com.gemframework.model.entity.vo;

import java.math.BigDecimal;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;

/**
 * @Title: ServiceSettingsVo
 * @Date: 2020-05-11 11:14:55
 * @Version: v1.0
 * @Description: 合同信息表VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class ServiceSettingsVo extends BaseEntityVo {

    /**
     * 服务名称
     */
    private String serviceName;

    private String type;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

}
