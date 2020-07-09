
package com.gemframework.model.entity.vo;

import java.math.BigDecimal;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;

/**
 * @Title: AssociatedServicesVo
 * @Date: 2020-05-19 09:07:48
 * @Version: v1.0
 * @Description: 关联服务VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class AssociatedServicesVo extends BaseEntityVo {

    /**
     * 服务编号
     */
    private String serviceId;
    private String serviceName;
    /**
     * 合同编号
     */
    private String contractId;
    /**
     * 价格
     */
    private BigDecimal price;

    private String type;

}
