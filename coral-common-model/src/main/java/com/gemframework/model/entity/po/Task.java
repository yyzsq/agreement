

package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @Title: Task
 * @Package: com.gemframework.model.entity.po
 * @Date: 2020-04-04 11:46:21
 * @Version: v1.0
 * @Description: 任务对象
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
@TableName("gem_sys_task")
public class Task extends BaseEntityPo {

	//任务名称
	private String name;
	//className名称
	private String className;
	//参数
	private String params;
	//cron表达式
	private String cron;
	//任务状态 0 暂停 1 开始 2 停止
	private Integer status;

}
