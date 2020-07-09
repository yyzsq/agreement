package com.gemframework.common.queue;

import com.gemframework.common.utils.GemRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * @Title: AbstractRedisMQConsumer
 * @Package: com.gemframework.common.queue
 * @Date: 2020-04-02 13:53:42
 * @Version: v1.0
 * @Description: 基于redis的模拟mq消费者，模板方法类
 * 使用时请扩展这个类，实现业务方法
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
public abstract class AbstractRedisMQConsumer<T> {

    @Autowired
    GemRedisUtils<GemQueueMessage<T>> redisUtils;
    /**
     * 读取redis超时时间
     */
    private int reidsReadTimeout = 30;
    /**
     * 消费者线程数
     */
    private int consumerThreadCount = 5;



    /***
     * 消息消费者
     * @param key
     */
    public void runConsumers(final String key){
        ExecutorService executor = Executors.newFixedThreadPool(consumerThreadCount);
        for(int i = 0 ; i < consumerThreadCount ; i ++){
            executor.submit(new Runnable() {
                @Override
                public void run() {
//                    while (true){
//                        if(getSize(key) > 0){
//                            GemQueueMessage<T> message = redisUtils.lBRightPop(key,reidsReadTimeout,TimeUnit.SECONDS);
//                            if(message != null){
//                                consume(message);
//                            }
//                        }else{
//                            log.info("====已经消费完====");
//                            executor.shutdown();
//                        }
//                    }
                    if(getSize(key) > 0){
                        GemQueueMessage<T> message = redisUtils.lBRightPop(key,reidsReadTimeout,TimeUnit.SECONDS);
                        if(message != null){
                            consume(message);
                        }
                    }else{
                        log.debug("====已经消费完====");
                    }
                }
            });
        }
        executor.shutdown();
    }

    public Long getSize(String key){
        return redisUtils.lLen(key);
    }

    public abstract void consume(GemQueueMessage<T> message);

    public void setConsumerThreadCount(int consumerThreadCount) {
        this.consumerThreadCount = consumerThreadCount;
    }

    public void setReidsReadTimeout(int reidsReadTimeout) {
        this.reidsReadTimeout = reidsReadTimeout;
    }
}
