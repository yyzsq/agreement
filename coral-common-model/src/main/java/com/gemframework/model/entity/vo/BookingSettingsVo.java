
package com.gemframework.model.entity.vo;

import java.math.BigDecimal;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;

/**
 * @Title: BookingSettingsVo
 * @Date: 2020-05-11 11:42:02
 * @Version: v1.0
 * @Description: 会计星级VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class BookingSettingsVo extends BaseEntityVo {

    /**
     * 星级名称
     */
    private String levenName;
    /**
     * 提成比例/金额
     */
    private String commission;
    private int agencyNumber;

    private int generalTaxNumber;

    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

}
