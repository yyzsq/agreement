
package com.gemframework.model.entity.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemframework.model.common.BaseEntityVo;
import com.gemframework.model.entity.po.AssociatedServices;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Title: ContractInfoVo
 * @Date: 2020-05-10 18:23:44
 * @Version: v1.0
 * @Description: 合同信息表VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class ContractInfoVo extends BaseEntityVo {

    /**
     *
     */
    private String contractId;

    private Long customerId;

    /**
     *
     */
    private String status;

    private BigDecimal discountMoney;

    private String giveMonthly;

    private String serviceMonthly;

    private String serviceId;
    /**
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date serviceTime;
    /**
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date serviceStartTime;
    /**
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date serviceEndTime;
    /**
     *
     */
    private BigDecimal amountMoney;

    private String services;

    private String serviceName;
    /**
     *
     */
    private BigDecimal amountReceived;
    /**
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date signTime;
    /**
     *
     */
    private String signedBy;
    /**
     *
     */
    private String chargingStandard;
    /**
     *
     */
    private String contractNature;
    /**
     *
     */
    private String isBill;
    /**
     *
     */
    private String accounting;
    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

    private String payType;

    private String customerName;

    private String signedName;

    private String accountingName;

    private String levelName;

    private List<AssociatedServices> servicesList;

    private BigDecimal weishouMoney;

    private String ids;

    private BigDecimal backMoney;

    private String backFlag;

    private BigDecimal bushouMoney;

    private BigDecimal thMoney;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date tingzhiTime;

    private String tichengId;

    private String tcType;

}
