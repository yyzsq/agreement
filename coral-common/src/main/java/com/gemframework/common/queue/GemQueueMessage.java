package com.gemframework.common.queue;

/**
 * @Title: GemQueueMessage
 * @Package: com.gemframework.common.queue
 * @Date: 2020-04-02 14:12:23
 * @Version: v1.0
 * @Description: 定义消息体
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
public interface GemQueueMessage<T> {

    void setData(T data);

    T getData();
}
