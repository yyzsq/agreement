
package com.gemframework.model.entity.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: ServiceCommission
 * @Date: 2020-05-28 11:46:31
 * @Version: v1.0
 * @Description: 业务提成实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_service_commission")
@Data
public class ServiceCommission extends BaseEntityPo {

    /**
     * 提成人
     */
    private String userId;
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

