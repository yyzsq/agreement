

package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

@Data
@TableName("gem_sys_task_logs")
public class TaskLogs extends BaseEntityPo{

	//任务ID
	private Long taskId;
	//错误消息
	private String error;
	//耗时(单位：毫秒)
	private Long times;
}
