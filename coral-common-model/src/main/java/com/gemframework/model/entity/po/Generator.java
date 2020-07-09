
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: Member
 * @Package: com.gemframework.model.entity.po
 * @Date: 2020-04-11 13:31:43
 * @Version: v1.0
 * @Description: 生成器配置实体对象
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@TableName("gem_sys_generator")
@Data
public class Generator extends BaseEntityPo {

    private String tabalePrefix;
    private String packageName;
    private String moduleName;
    private String authorName;
    private String authorEmail;
}