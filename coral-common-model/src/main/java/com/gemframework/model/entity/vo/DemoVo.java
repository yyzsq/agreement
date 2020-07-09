
package com.gemframework.model.entity.vo;

import com.gemframework.model.common.BaseEntityVo;
import com.gemframework.model.common.validator.SuperValidator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.validation.constraints.NotBlank;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class DemoVo extends BaseEntityVo{
	//名称
	@NotBlank(message = "名称不能为空",groups = SuperValidator.class)
	private String name;
	//内容
	private String content;
	//状态
	private String status;
}
