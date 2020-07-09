
package com.gemframework.model.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;


/**
 * @Title: Right
 * @Package: com.gemframework.model.entity.po
 * @Date: 2020-03-13 14:22:15
 * @Version: v1.0
 * @Description: 权限信息
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@TableName("gem_sys_right")
@Data
public class Right extends BaseEntityPo {

    //父级ID
    private Long pid;
    //名称
    private String name;
    //标识组 多个以逗号隔开
    private String flags;
    //图标
    private String icon;
    //链接
    private String link;
    //类型 0 菜单 1 按钮 2 权限
    private Integer type;
    //级别
    private Integer level;
    //位置 0 左侧 1 顶部 2 底部
    private Integer position;

}