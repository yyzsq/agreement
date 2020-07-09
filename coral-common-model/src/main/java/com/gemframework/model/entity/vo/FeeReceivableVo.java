
package com.gemframework.model.entity.vo;

import java.math.BigDecimal;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;

/**
 * @Title: FeeReceivableVo
 * @Date: 2020-05-20 20:27:17
 * @Version: v1.0
 * @Description: 收费管理VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class FeeReceivableVo extends BaseEntityVo {

    /**
     * 客户编号
     */
    private String customerId;

    private String customerName;
    /**
     * 金额
     */
    private BigDecimal money;
    /**
     * 合同/业务编号
     */
    private String guanLianId;
    /**
     * 业务人员
     */
    private String userId;

    private String userName;
    /**
     * 来源类型
     */
    private String type;
    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

    private String status;

    private String payType;



}
