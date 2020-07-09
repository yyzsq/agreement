
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;


@TableName("gem_sys_role")
@Data
public class Role extends BaseEntityPo {
    private String name;
    private String flag;
    private Integer dataRange;
}