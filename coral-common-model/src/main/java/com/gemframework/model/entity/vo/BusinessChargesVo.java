
package com.gemframework.model.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemframework.model.common.BaseEntityVo;
import com.gemframework.model.entity.po.AssociatedServices;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Title: BusinessChargesVo
 * @Date: 2020-05-20 17:44:45
 * @Version: v1.0
 * @Description: 业务收费表VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class BusinessChargesVo extends BaseEntityVo {

    private String serviceId;
    /**
     * 客户编号
     */
    private String customerId;

    private String customerName;

    private String discountMoney;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date shouFeiTime;
    /**
     * 业务人员
     */
    private String userId;

    private String userName;
    /**
     * 办事人员
     */
    private String clerksId;

    private String clerksName;
    /**
     *
     */
    private BigDecimal money;

    private String status;

    private String other1;
    /**
     *
     */
    private String other2;

    private String services;

    private String payType;

    private List<AssociatedServices> servicesList;

    private ContractInfoVo contractInfoVo;

    private Boolean tkFlag;

    private String tichengId;


}
