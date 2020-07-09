
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: HandoverRecord
 * @Date: 2020-05-15 10:31:46
 * @Version: v1.0
 * @Description: 交接记录实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_handover_record")
@Data
public class HandoverRecord extends BaseEntityPo {

    /**
     * 转接人
     */
    private String accounting;
    /**
     * 接收人
     */
    private String receiver;
    /**
     * 客户编号
     */
    private String customerId;

    private String contractId;

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

