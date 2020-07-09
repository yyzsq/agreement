
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;


@TableName("gem_sys_role_depts")
@Data
public class RoleDepts extends BaseEntityPo {
    private Long roleId;
    private Long deptId;
}