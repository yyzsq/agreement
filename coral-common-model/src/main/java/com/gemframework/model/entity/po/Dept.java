
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;


@TableName("gem_sys_dept")
@Data
public class Dept extends BaseEntityPo {

    private Long pid;
    private String name;
    private String fullname;
    private Integer type;
    private Integer level;
}