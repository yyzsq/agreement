
package com.gemframework.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gemframework.model.entity.po.WechatNotice;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Title: WechatNoticeMapper
 * @Date: 2020-05-13 11:57:21
 * @Version: v1.0
 * @Description: 微信通知绑定持久层
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Repository
public interface WechatNoticeMapper extends BaseMapper<WechatNotice> {

    public List<WechatNotice> findList(String customerName);
}