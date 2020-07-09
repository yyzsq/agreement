

package com.gemframework.model.entity.vo;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Title: TaskVo
 * @Package: com.gemframework.model.entity.vo
 * @Date: 2020-04-04 11:55:48
 * @Version: v1.0
 * @Description: 任务对象
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class TaskVo extends BaseEntityVo {

	//任务名称
	@NotBlank(message = "任务名称不能为空")
	private String name;
	//className名称
	@NotBlank(message = "ClassName名称不能为空")
	private String className;
	//参数
	private String params;
	//cron表达式
	@NotBlank(message = "cron表达式不能为空")
	private String cron;
	//任务状态
	private Integer status;

}
