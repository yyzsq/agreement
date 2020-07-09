

package com.gemframework.annotation;

import com.gemframework.model.enums.OperateType;

import java.lang.annotation.*;

/**
 * @Title: Log
 * @Package: com.gemframework.annotation
 * @Date: 2020-04-02 15:40:31
 * @Version: v1.0
 * @Description: 这里写描述
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

	//0 操作日志 1 登录日志
	OperateType type() default OperateType.NORMAL;

	String value() default "";

	/**查询的bean名称*/
	String serviceClass() default "";

	/**查询单个详情的bean的方法*/
	String queryMethod() default "";

	/**查询详情的参数类型*/
	String parameterType() default "";

	/**从页面参数中解析出要查询的id，
	* 如域名修改中要从参数中获取customerDomainId的值进行查询
	*/
	String parameterKey() default "";

	/**是否为批量类型操作*/
	boolean paramIsArray() default false;
}
