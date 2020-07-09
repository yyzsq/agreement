
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: CustomerResearch
 * @Date: 2020-05-10 17:39:43
 * @Version: v1.0
 * @Description: 客户信息表实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_customer_research")
@Data
public class CustomerResearch extends BaseEntityPo {

    /**
     *
     */
    private String corporateName;
    /**
     *
     */
    private String corporateId;
    /**
     *
     */
    private String corporationName;
    /**
     *
     */
    private String phone;

    private String services;

    private String personCharge;

    private String payTaxProperties;

    private String declareType;
    /**
     *
     */
    private String phone2;
    /**
     *
     */
    private String address;

    private String isAddres;

    private String addresDetail;

    private String mainBusiness;

    /**
     *
     */
    private String script;
    /**
     *
     */
    private String copy;
    /**
     *
     */
    private String taxInfo;
    /**
     *
     */
    private String enclosure;
    /**
     *
     */
    private String isBill;
    /**
     *
     */
    private String levyFees;
    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

}

