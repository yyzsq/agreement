
package com.gemframework.model.entity.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: ServiceSettings
 * @Date: 2020-05-11 11:14:55
 * @Version: v1.0
 * @Description: 合同信息表实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_service_settings")
@Data
public class ServiceSettings extends BaseEntityPo {

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

