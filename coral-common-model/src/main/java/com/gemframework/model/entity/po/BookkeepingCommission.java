
package com.gemframework.model.entity.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: BookkeepingCommission
 * @Date: 2020-05-15 16:19:14
 * @Version: v1.0
 * @Description: 培训报名记录表实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_bookkeeping_commission")
@Data
public class BookkeepingCommission extends BaseEntityPo {

    /**
     * 会计编号
     */
    private String userId;
    /**
     * 所属星级
     */
    private String levenName;
    /**
     * 提成比例
     */
    private String percentage;

    private BigDecimal countMoney;

    //记账数量
    private int agencyNumber;
    //记账类型
    private int generalTaxNumber;
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

