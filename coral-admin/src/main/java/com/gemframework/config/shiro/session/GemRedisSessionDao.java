
package com.gemframework.config.shiro.session;

import com.gemframework.common.utils.GemRedisUtils;
import com.gemframework.common.utils.GemSerializeUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Title: GemRedisSessionDao
 * @Package: com.gemframework.config.shiro.session
 * @Date: 2020-04-18 21:42:55
 * @Version: v1.0
 * @Description: 自定义SessionDao
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Component
public class GemRedisSessionDao extends AbstractSessionDAO {

    @Autowired
    GemRedisUtils<String> gemRedisUtils;

    private final String SHIRO_SESSION_PERFIX = "shiro-session:";
    private String getKey(String key){
        return SHIRO_SESSION_PERFIX + key;
    }

    /**
     * 保存Session
     * @param session
     * @throws IOException
     */
    protected void saveSession(Session session) throws IOException {
        if(session != null && session.getId() != null){
            String key = getKey(session.getId().toString());
            gemRedisUtils.set(key, GemSerializeUtils.serialize(session));
            gemRedisUtils.expire(key,30, TimeUnit.MINUTES);
        }
    }

    /**
     * 创建Session
     * @param session
     * @return
     */
    @SneakyThrows
    @Override
    protected Serializable doCreate(Session session) {
        SessionIdGenerator sessionIdGenerator = getSessionIdGenerator();
        Serializable sessionId = sessionIdGenerator.generateId(session);
        assignSessionId(session,sessionId);
        saveSession(session);
        return sessionId;
    }

    /**
     * 读取Session
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if(sessionId == null){
            return null;
        }
        String key = getKey(sessionId.toString());
        Session session = null;
        String value = gemRedisUtils.get(key);
        if(value != null){
            try {
                session = (Session) GemSerializeUtils.serializeToObject(value);
                //log.info("读取key:"+key+"==session:"+session);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return session;
    }


    /**
     * 更新Session
     * @param session
     * @throws UnknownSessionException
     */
    @SneakyThrows
    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    /**
     * 删除session
     * @param session
     */
    @Override
    public void delete(Session session) {
        if(session == null || session.getId() == null){
            return;
        }
        String key = getKey(session.getId().toString());
        gemRedisUtils.delete(key);
    }

    /**
     * 获取所有存活的seesion
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        Set<String> keys = gemRedisUtils.keys(SHIRO_SESSION_PERFIX+"*");
        Set<Session>  sessions = new HashSet<>();
        if(keys == null && keys.isEmpty()){
            return sessions;
        }
        for(String key : keys){
            Session session = null;
            try {
                session = (Session) GemSerializeUtils.serializeToObject(gemRedisUtils.get(key));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            sessions.add(session);
        }
        return sessions;
    }
}
