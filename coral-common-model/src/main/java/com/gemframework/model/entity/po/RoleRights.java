
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;


@TableName("gem_sys_role_rights")
@Data
public class RoleRights extends BaseEntityPo {
    private Long roleId;
    private Long rightId;
}