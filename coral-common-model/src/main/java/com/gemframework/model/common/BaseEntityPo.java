
package com.gemframework.model.common;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: BasePo.java
 * @Package: com.gemframework.model.po
 * @Date: 2019/11/28 22:15
 * @Version: v1.0
 * @Description: PO基类
 * @Author: zhangysh
 * @Copyright: Copyright (c) 2019 GemStudio
 * @Company: www.gemframework.com
 */

@Data
public class BaseEntityPo implements Serializable {

    @TableId
    private Long id;
    //排序
    private Integer sortNumber;
    //备注字段
    private String remark;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime = new Date();
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime = new Date();

    //是否删除
    private int deleted;

}