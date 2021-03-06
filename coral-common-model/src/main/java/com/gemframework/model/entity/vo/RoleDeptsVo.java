
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
public class RoleDeptsVo extends BaseEntityVo {

    @NotNull(message = "roleId不能为空",groups = SuperValidator.class)
    private Long roleId;

    private Long deptId;

    @NotBlank(message = "deptIds不能为空",groups = SuperValidator.class)
    private String deptIds;

}