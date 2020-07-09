
package com.gemframework.model.entity.vo;

import com.gemframework.model.common.BaseEntityVo;
import com.gemframework.model.common.validator.SuperValidator;
import com.gemframework.model.common.validator.UpdateValidator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class DeptVo extends BaseEntityVo {

    private Long pid;

    @NotBlank(message = "名称不能为空",groups = SuperValidator.class)
    private String name;
    @NotBlank(message = "全称不能为空",groups = SuperValidator.class)
    private String fullname;
    private Integer type;
    private Integer level;
}