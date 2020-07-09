
package com.gemframework.model.entity.po;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: UploadFile
 * @Date: 2020-05-16 14:13:04
 * @Version: v1.0
 * @Description: 关联图片地址实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_upload_file")
@Data
public class UploadFile extends BaseEntityPo {

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

