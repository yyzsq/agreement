
package com.gemframework.model.entity.vo;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;

/**
 * @Title: HandoverRecordVo
 * @Date: 2020-05-15 10:31:46
 * @Version: v1.0
 * @Description: 交接记录VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class HandoverRecordVo extends BaseEntityVo {

    /**
     * 转接人
     */
    private String accounting;

    private String deliverPersonName;
    /**
     * 接收人
     */
    private String receiver;

    private String receiverName;

    private String customerId;

    private String contractId;
    /**
     * 客户编号
     */
    private String customerName;

    private String status;
    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

}
