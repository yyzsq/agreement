
package com.gemframework.model.entity.po;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Title: ContractInfo
 * @Date: 2020-05-10 18:23:44
 * @Version: v1.0
 * @Description: 合同信息表实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_contract_info")
@Data
public class ContractInfo extends BaseEntityPo {

    /**
     *
     */
    private String contractId;

    private Long customerId;

    private String customerName;

    private BigDecimal discountMoney;
    /**
     *
     */
    private String status;

    private String serviceId;
    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date serviceTime;
    /**
     *
     */

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date serviceStartTime;
    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date serviceEndTime;
    /**
     *
     */

    private BigDecimal amountMoney;

    private String services;
    /**
     *
     */
    private BigDecimal amountReceived;
    /**
     *
     */
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

    private String giveMonthly;

    private String serviceMonthly;
    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

    private String payType;

    private BigDecimal weishouMoney;

    private BigDecimal backMoney;

    private BigDecimal bushouMoney;

    private BigDecimal thMoney;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date tingzhiTime;
}

