
package com.gemframework.generator.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Title: Tables
 * @Package: com.gemframework.generator
 * @Date: 2020-04-14 19:00:01
 * @Version: v1.0
 * @Description: 数据库表
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
@Builder
public class Tables {
	private String tableName;
	private String comments;
	//编码
	private String tableCollation;
	//引擎
	private String engine;
	//主键
	private Columns pk;
	//列（不含主键）
	private List<Columns> columns;
	//如：User  SysLog
	private String classNameUp;
	//如：user  sysLog
	private String classNameLow;
}
