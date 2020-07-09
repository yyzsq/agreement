package com.gemframework.common.queue;

import com.alibaba.fastjson.JSON;
import com.gemframework.common.utils.GemRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Title: RedisMQProducer
 * @Package: com.gemframework.common.queue
 * @Date: 2020-04-02 13:55:17
 * @Version: v1.0
 * @Description: 使用Redis模拟消息队列-消息生产者
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Component
public class RedisMQProducer<T> {

    @Autowired
    GemRedisUtils<GemQueueMessage<T>> redisUtils;

    /**
     * 发送mq消息,注意处理异常
     * @param key
     * @param message
     */
    public void send(String key , GemQueueMessage<T> message) throws Exception{
        if(StringUtils.isBlank(key) || message == null) return ;
        log.info("生产key="+key+"="+ JSON.toJSONString(message));
        redisUtils.lLeftPush(key,message);
    }
}

