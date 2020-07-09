package com.gemframework.service.queue;
import com.alibaba.fastjson.JSON;
import com.gemframework.common.queue.AbstractRedisMQConsumer;
import com.gemframework.common.queue.GemQueueMessage;
import com.gemframework.model.entity.po.SysLogs;
import com.gemframework.service.SysLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Map;

import static com.gemframework.common.constant.GemRedisKeys.Queue.LOG_SYNC_DB;
import static com.gemframework.common.constant.GemRedisKeys.Queue.LOG_SYNC_DB_SAVE;


/**
 * @Title: LogsSyncQueueConsumer
 * @Package: com.gemframework.service.queue
 * @Date: 2020-04-02 15:15:47
 * @Version: v1.0
 * @Description: 系统日志异步消费队列
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Component
@EnableScheduling
public class LogsRedisMQConsumer extends AbstractRedisMQConsumer<Map<String,Object>> {

    private static final int consumerThreadCount = 1;

    private static final int reidsReadTimeout = 30;

    @Autowired
    SysLogsService sysLogsService;


    /**
     * 10秒执行一次任务
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void taskRun(){
        log.debug("********sync syslogs job is ok******");
        //设置线程数,可以配在文件里
        setConsumerThreadCount(consumerThreadCount);
        setReidsReadTimeout(reidsReadTimeout);
        //启动消费者
        runConsumers(LOG_SYNC_DB);
    }

    /**
     * 实现具体的消费逻辑
     * @param message
     */
    @Override
    public void consume(GemQueueMessage<Map<String,Object>> message) {
        Map<String,Object> map = message.getData();
        log.info("消费===="+ JSON.toJSONString(map));
        if(map.get(LOG_SYNC_DB_SAVE) != null){
            //TODO: 实现相关业务 异步保存数据库
            log.info("map.data="+map.get(LOG_SYNC_DB_SAVE).toString());
            SysLogs sysLogs = (SysLogs) map.get(LOG_SYNC_DB_SAVE);
            sysLogsService.save(sysLogs);
        }
    }
}
