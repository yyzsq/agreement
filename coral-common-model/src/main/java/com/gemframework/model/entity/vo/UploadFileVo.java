
package com.gemframework.model.entity.vo;

import com.gemframework.model.common.BaseEntityVo;
import lombok.Data;

/**
 * @Title: UploadFileVo
 * @Date: 2020-05-16 14:13:04
 * @Version: v1.0
 * @Description: 关联图片地址VO对象
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Data
public class UploadFileVo extends BaseEntityVo {

											/**
			 * 图片地址
			 */
			private String imgUrl;
								/**
			 * 图片名称
			 */
			private String imgName;
								/**
			 * 图片类型
			 */
			private String imgType;
								/**
			 * 关联编号
			 */
			private String relationId;
																												
}
