
package com.gemframework.aspect;
import com.gemframework.annotation.Log;
import com.gemframework.common.queue.GemQueueMessage;
import com.gemframework.common.queue.RedisMQProducer;
import com.gemframework.common.utils.GemHttpUtils;
import com.gemframework.common.utils.GemIPHandler;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.entity.po.SysLogs;
import com.gemframework.model.enums.OperateStatus;
import com.gemframework.service.queue.MapQueueMessage;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.gemframework.common.constant.GemRedisKeys.Queue.LOG_SYNC_DB;
import static com.gemframework.common.constant.GemRedisKeys.Queue.LOG_SYNC_DB_SAVE;

@Component  //声明组件
@Aspect //  声明切面
@ComponentScan  //组件自动扫描
@EnableAspectJAutoProxy //spring自动切换JDK动态代理和CGLIB
@Slf4j
public class LogAspect {
    private long startTime;
    private long endTime;
    private String username;

    @Autowired
    RedisMQProducer<Map<String, Object>> redisMQProducer;

    /**
     * 在方法执行前进行切面
     */
    @Pointcut("execution(* com.gemframework.controller..*.*(..))" +
            "&& (@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.RequestMapping))")
    public void log() {
    }


    /**
     * 程序执行之前
     */
    @Before("log()")
    public void before() {
        this.startTime = System.currentTimeMillis();
        this.username = (String) SecurityUtils.getSubject().getPrincipal();
        log.debug("程序执行前");
    }

    /**
     * 程序执行之后
     */
    @After("log()")
    public void after(){
        this.endTime = System.currentTimeMillis();
        log.debug("程序执行后");
    }


    @Around("log()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        this.endTime = System.currentTimeMillis();
        //保存日志
        saveLog(point, result, (endTime - startTime));
        log.debug("程序执行时间 {}-{}={},运行结果 {}", endTime,startTime,endTime - startTime,result);
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, Object result, long time) throws Exception {
        //获取request
        HttpServletRequest request = GemHttpUtils.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogs sysLogs = new SysLogs();
        Log annotationLog = method.getAnnotation(Log.class);
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        if(StringUtils.isBlank(username)){
            username = this.username;
        }
        if(annotationLog != null && StringUtils.isNotBlank(username)){
            //设置当前用户
            sysLogs.setUsername(username);
            //设置操作内容
            sysLogs.setOperation(annotationLog.value());
            //请求的方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
            //设置操方法名
            sysLogs.setMethod(className + "." + methodName + "()");
            //请求的参数
            Object[] args = joinPoint.getArgs();
            if(args.length > 0){
                String params = new Gson().toJson(args[0]);
                //设置参数名
                sysLogs.setParams(params);
            }
            //设置操作状态
            sysLogs.setStatus(OperateStatus.SUCCESS.getCode());
            sysLogs.setStatusCode(OperateStatus.SUCCESS.getCode());
            sysLogs.setStatusMsg(result.toString());
            if(result instanceof BaseResultData){
                //将object 转化为controller封装返回的实体类：RequestResult
                BaseResultData resultData = (BaseResultData) result;
                sysLogs.setStatusCode(resultData.getCode());
                sysLogs.setStatusMsg(resultData.getMsg());
                if (resultData.getCode() == 0) {
                    sysLogs.setStatus(OperateStatus.SUCCESS.getCode());
                } else {
                    sysLogs.setStatus(OperateStatus.FAIL.getCode());
                }
            }
            //设置IP信息
            sysLogs.setUserip(GemIPHandler.getIpAddr(request));

            sysLogs.setTimes(time);
            sysLogs.setCreateTime(new Date());
            sysLogs.setUpdateTime(new Date());
            GemQueueMessage<Map<String,Object>> mapQueueMessage = new MapQueueMessage();
            Map<String,Object> map = new HashMap<>();
            map.put(LOG_SYNC_DB_SAVE,sysLogs);
            mapQueueMessage.setData(map);
            //向key为cacheKey的队列发送消息
            redisMQProducer.send(LOG_SYNC_DB,mapQueueMessage);
        }
    }
}