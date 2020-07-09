
package com.gemframework.model.entity.vo;

import com.gemframework.model.common.BaseEntityVo;
import com.gemframework.model.common.validator.SuperValidator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class UserRolesVo extends BaseEntityVo {

    @NotNull(message = "userId不能为空",groups = SuperValidator.class)
    private Long userId;

    private Long roleId;

    @NotBlank(message = "roleIds不能为空",groups = SuperValidator.class)
    private String roleIds;

}