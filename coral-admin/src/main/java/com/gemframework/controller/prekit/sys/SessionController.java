
package com.gemframework.controller.prekit.sys;

import com.gemframework.annotation.Log;
import com.gemframework.config.shiro.session.GemRedisSessionDao;
import com.gemframework.constant.GemModules;
import com.gemframework.controller.prekit.BaseController;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.entity.vo.SessionVo;
import com.gemframework.model.enums.OperateType;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.gemframework.common.constant.GemSessionKeys.*;


/**
 * @Title: SessionController
 * @Date: 2020-04-18 21:24:41
 * @Version: v1.0
 * @Description: 系统会话表控制器
 * @Author: gem
 * @Email: gemframe@163.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@RestController
@RequestMapping(GemModules.PreKit.PATH_SYSTEM+"/session")
public class SessionController extends BaseController {

    private static final String moduleName = "在线用户";

    @Autowired
    GemRedisSessionDao gemRedisSessionDao;

    /**
     * 获取在线用户列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("session:list")
    public BaseResultData list() {
        List<SessionVo> list = new ArrayList<>();
        Collection<Session> sessions = gemRedisSessionDao.getActiveSessions();
        for (Session session : sessions) {
            SessionVo online = new SessionVo();
            SimplePrincipalCollection principalCollection;
            if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY )== null){
                continue;
            }else {
                principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                String username = (String) principalCollection.getPrimaryPrincipal();
                online.setUsername(username);
            }

            online.setUserIp(session.getHost());
            online.setSessionId((String) session.getId());
            online.setStartAccessTime(session.getStartTimestamp());
            online.setLastAccessTime(session.getLastAccessTime());
            online.setTimeOut(session.getTimeout());
            online.setBrowser((String) session.getAttribute(USER_BROWSER_KEY));
            online.setClientOs((String) session.getAttribute(USER_OS_KEY));
            online.setClientDevice((String) session.getAttribute(USER_DEVICE_KEY));
            list.add(online);
        }
        return BaseResultData.SUCCESS(list);
    }


    /**
     * 强制踢掉
     * @return
     */
    @Log(type = OperateType.ALTER,value = "强制踢掉"+moduleName)
    @PostMapping("/kickout")
    @RequiresPermissions("session:kickout")
    public BaseResultData kickout(String sessionId) {
        Session session = gemRedisSessionDao.readSession(sessionId);
        gemRedisSessionDao.delete(session);
        return BaseResultData.SUCCESS();
    }




}