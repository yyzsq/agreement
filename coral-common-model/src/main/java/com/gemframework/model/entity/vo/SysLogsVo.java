
package com.gemframework.model.entity.vo;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class SysLogsVo extends BaseEntityVo{

	@NotBlank(message = "用户名不能为空")
	private String username;
	private String operation;
	private String method;
	private String params;
	private Long times;
	private String userip;
	//请求前参数
	private String beforeParams;
	//操作执行状态信息
	private Integer status;
	private Integer statusCode;
	private String statusMsg;
}
