
package com.gemframework.config.shiro.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @Title: RedisSessionDao
 * @Package: com.gemframework.config.session
 * @Date: 2020-03-29 10:39:48
 * @Version: v1.0
 * @Description: 父级提供了缓存功能，实现Redis持久化方案
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
public class GemCacheSessionDao extends EnterpriseCacheSessionDAO {

    @Autowired
    private GemRedisSessionDao gemRedisSessionDao;

    @Override
    protected Serializable doCreate(Session session) {
        log.debug("Create-SessionId:"+session);
        return gemRedisSessionDao.doCreate(session);
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {

        log.debug("Read-SessionId:"+sessionId);
        return gemRedisSessionDao.doReadSession(sessionId);
    }

    @Override
    protected void doUpdate(Session session) {
        log.debug("Update-Session:"+session);
        gemRedisSessionDao.update(session);
    }

    @Override
    protected void doDelete(Session session) {
        log.debug("Delete-Session:"+session);
        gemRedisSessionDao.delete(session);
    }
}
