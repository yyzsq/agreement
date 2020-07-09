

package com.gemframework.model.entity.vo;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Title: TaskLogsVo
 * @Package: com.gemframework.model.entity.vo
 * @Date: 2020-04-04 11:55:36
 * @Version: v1.0
 * @Description: TaskLogsVo
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class TaskLogsVo extends BaseEntityVo{

	//任务ID
	@NotNull(message = "任务ID不能为空")
	private Long taskId;
	//错误消息
	private String error;
	//耗时(单位：毫秒)
	private Long times;
}
