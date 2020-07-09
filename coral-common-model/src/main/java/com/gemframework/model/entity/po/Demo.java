
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;


@TableName("gem_demo")
@Data
public class Demo extends BaseEntityPo{
	//名称
	private String name;
	//内容
	private String content;
	//状态
	private String status;
}
