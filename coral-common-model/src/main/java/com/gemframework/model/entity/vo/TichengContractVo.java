
package com.gemframework.model.entity.vo;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;

/**
 * @Title: TichengContractVo
 * @Date: 2020-06-22 11:29:17
 * @Version: v1.0
 * @Description: 提成关联合同编号VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class TichengContractVo extends BaseEntityVo {

    /**
     * 关联编号
     */
    private String guanLianId;

    private String tiChengId;
    /**
     * 类型：0-记账提成
     */
    private String type;
    /**
     *
     */
    private String other1;
    /**
     *
     */
    private String other2;

}
