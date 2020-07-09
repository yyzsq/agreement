
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: Dictionary
 * @Date: 2020-04-16 23:32:51
 * @Version: v1.0
 * @Description: 字典表
 * @Author: gem
 * @Email: gemframe@163.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("gem_sys_dictionary")
@Data
public class Dictionary extends BaseEntityPo {

    /**
     * 名称
     */
    private String name;
    /**
     * 类型 1 k-v 2 k-map options
     */
    private Integer type;
    /**
     * 键
     */
    private String keyName;
    /**
     * 值
     */
    private String valueStr;
    /**
     * 加密方式 1明文 2密文
     */
    private String encrypMode;

}

