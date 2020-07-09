
package com.gemframework.model.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Title: WechatJiluVo
 * @Date: 2020-05-31 12:57:32
 * @Version: v1.0
 * @Description: 微信通知记录VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class WechatJiluVo extends BaseEntityVo {

    /**
     * 合同编号
     */
    private String contractId;
    /**
     * 发送状态
     */
    private String fsStatus;
    /**
     * 发送类型
     */
    private String fsType;
    /**
     * 发送时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date fsTime;
    /**
     * 发送人
     */
    private String jsRen;

}
