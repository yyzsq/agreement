
package com.gemframework.model.entity.vo;

import java.math.BigDecimal;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;

/**
 * @Title: ServiceCommissionVo
 * @Date: 2020-05-28 11:46:31
 * @Version: v1.0
 * @Description: 业务提成VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class ServiceCommissionVo extends BaseEntityVo {

    /**
     * 提成人
     */
    private String userId;

    private String userName;
    /**
     * 提成类型
     */
    private String type;
    /**
     * 提成比例
     */
    private String proportion;
    /**
     * 提成金额
     */
    private BigDecimal money;
    /**
     * 提成时间
     */
    private String date;
    /**
     * 总金额
     */
    private BigDecimal countMoney;

    private String danNumber;
    /**
     * 其他1
     */
    private String other1;
    /**
     * 其他2
     */
    private String other2;

}
