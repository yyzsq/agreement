
package com.gemframework.model.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemframework.model.common.validator.SuperValidator;
import com.gemframework.model.common.validator.UpdateValidator;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Title: BasePo.java
 * @Package: com.gemframework.model.po
 * @Date: 2019/11/28 22:15
 * @Version: v1.0
 * @Description: VO基类
 * @Author: zhangysh
 * @Copyright: Copyright (c) 2019 GemStudio
 * @Company: www.gemframework.com
 */

@Data
public abstract class BaseEntityVo implements Serializable {

    @NotNull(message = "ID不能为空",groups = {UpdateValidator.class})
    private Long id;
    //排序
    private Integer sortNumber;
    //备注字段
    private String remark;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


    //是否删除
    private int deleted;

    /**
     * 用于查询条件
     */
    //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String startDate;
    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endDate;
    //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;
    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;
}