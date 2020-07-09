
package com.gemframework.model.entity.po;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: AssociatedServices
 * @Date: 2020-05-19 09:07:48
 * @Version: v1.0
 * @Description: 关联服务实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_associated_services")
@Data
public class AssociatedServices extends BaseEntityPo {

										/**
		 * 服务编号
		 */
		private String serviceId;
							/**
		 * 合同编号
		 */
		private String contractId;
							/**
		 * 价格
		 */
		private BigDecimal price;

		private String type;
																												
}

