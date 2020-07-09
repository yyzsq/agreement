
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: BusinessCharges
 * @Date: 2020-05-20 17:44:45
 * @Version: v1.0
 * @Description: 业务收费表实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_business_charges")
@Data
public class BusinessCharges extends BaseEntityPo {

    private String serviceId;

    /**
     * 客户编号
     */
    private String customerId;

    private String customerName;

    private String discountMoney;
    /**
     * 业务人员
     */
    private String userId;
    /**
     * 办事人员
     */
    private String clerksId;

    private String status;

    private BigDecimal money;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date shouFeiTime;
    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

    private String payType;

}

