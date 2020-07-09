
package com.gemframework.model.entity.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;

/**
 * @Title: CollectionInfoVo
 * @Date: 2020-05-11 14:19:02
 * @Version: v1.0
 * @Description: 催费报表VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class CollectionInfoVo extends BaseEntityVo {

    /**
     * 记账会计
     */
    private String userId;

    private String userName;
    /**
     * 公司编号
     */
    private String firmId;

    private String firmName;

    private String collectionId;
    /**
     * 收费金额
     */
    private BigDecimal price;
    /**
     * 上次收费时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date upTime;
    /**
     * 下次收费时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date nextTime;
    /**
     * 收款状态
     */
    private String status;
    /**
     * 合同到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expirationDate;
    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

}
