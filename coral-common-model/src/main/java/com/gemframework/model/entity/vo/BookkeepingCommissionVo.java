
package com.gemframework.model.entity.vo;

import java.math.BigDecimal;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;

/**
 * @Title: BookkeepingCommissionVo
 * @Date: 2020-05-15 16:19:14
 * @Version: v1.0
 * @Description: 培训报名记录表VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class BookkeepingCommissionVo extends BaseEntityVo {

    /**
     * 会计编号
     */
    private String userId;

    private String userName;
    /**
     * 所属星级
     */
    private String levenName;
    /**
     * 提成比例
     */
    private String percentage;

    private Integer agencyNumber;

    private Integer generalTaxNumber;

    private BigDecimal countMoney;
    /**
     * 提成金额
     */
    private BigDecimal money;
    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

}
