
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;


@TableName("gem_sys_logs")
@Data
public class SysLogs extends BaseEntityPo{
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
