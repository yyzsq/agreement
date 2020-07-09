
package com.gemframework.model.entity.po;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

import java.util.Date;

/**
 * @Title: WechatJilu
 * @Date: 2020-05-31 12:57:32
 * @Version: v1.0
 * @Description: 微信通知记录实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_wechat_jilu")
@Data
public class WechatJilu extends BaseEntityPo {

										/**
		 * 合同编号
		 */
		private String contractId;
							/**
		 * 发送状态
		 */
		private String fsStatus;
							/**
		 * 发送类型
		 */
		private String fsType;
							/**
		 * 发送时间
		 */
		private Date fsTime;
							/**
		 * 发送人
		 */
		private String jsRen;
																												
}

