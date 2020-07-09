
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;


@TableName("gem_sys_user_roles")
@Data
public class UserRoles extends BaseEntityPo {
    private Long userId;
    private Long roleId;
}