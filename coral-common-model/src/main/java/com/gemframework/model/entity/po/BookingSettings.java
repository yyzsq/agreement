
package com.gemframework.model.entity.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: BookingSettings
 * @Date: 2020-05-11 11:42:02
 * @Version: v1.0
 * @Description: 会计星级实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_booking_settings")
@Data
public class BookingSettings extends BaseEntityPo {

    /**
     * 星级名称
     */
    private String levenName;

    private int agencyNumber;

    private int generalTaxNumber;
    /**
     * 提成比例/金额
     */
    private String commission;

    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

}

