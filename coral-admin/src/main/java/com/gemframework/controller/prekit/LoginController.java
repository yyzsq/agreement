
package com.gemframework.controller.prekit;

import com.gemframework.annotation.Log;
import com.gemframework.common.constant.GemRedisKeys;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.model.request.UserLoginRequest;
import com.gemframework.utils.Base64Utils;
import com.gemframework.utils.VerifyCodeUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.Set;

import static com.gemframework.common.constant.GemSessionKeys.*;
import static com.gemframework.model.enums.ErrorCode.*;

@Slf4j
@Controller
public class LoginController extends BaseController{


    @Log(type = OperateType.LOGIN,value = "用户登录")
    @PostMapping(value = "/login")
    @ResponseBody
    public BaseResultData login(UserLoginRequest loginRequest, HttpServletRequest request) {
        //验证码校验
        if(!VerifyCodeUtils.checkVerifyCode(request)){
            return BaseResultData.ERROR(ErrorCode.VERIFY_CODE_ERROR);
        }else{
            log.info("验证码验证通过...");
        }
        // 创建主体
        Subject subject = SecurityUtils.getSubject();
        // 准备token
        String password = Base64Utils.decode(loginRequest.getPassword());
        UsernamePasswordToken token = new UsernamePasswordToken(loginRequest.getUsername(),password,loginRequest.isRememberMe());
        try {
            // 提交认证
            subject.login(token);
        } catch (UnknownAccountException uae) {
            return BaseResultData.ERROR(LOGIN_FAIL_UNKNOWNACCOUNT);
        } catch (IncorrectCredentialsException ice) {
            return BaseResultData.ERROR(LOGIN_FAIL_INCORRECTCREDENTIALS);
        } catch (LockedAccountException lae) {
            return BaseResultData.ERROR(LOGIN_FAIL_LOCKEDACCOUNT);
        } catch (ExcessiveAttemptsException eae) {
            return BaseResultData.ERROR(LOGIN_FAIL_EXCESSIVEATTEMPTS);
        } catch (AuthenticationException ae) {
            return BaseResultData.ERROR(LOGIN_FAIL_AUTHENTICATION);
        }
        if (subject.isAuthenticated()) {
            log.info("登录成功...");
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            //获取浏览器信息 
            Browser userbrowser = userAgent.getBrowser();
            //获取操作系统信息
            OperatingSystem useros = userAgent.getOperatingSystem();
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute(USER_BROWSER_KEY,userbrowser.getName()+";"
                    +userbrowser.getRenderingEngine().getName()+";"
                    +userbrowser.getManufacturer().getName());
            session.setAttribute(USER_DEVICE_KEY,useros.getDeviceType().getName());
            session.setAttribute(USER_OS_KEY,useros.name());
            return BaseResultData.SUCCESS("登录成功");
        } else {
            token.clear();
            return BaseResultData.ERROR(LOGIN_FAIL);
        }
    }

    @Log(type = OperateType.LOGIN,value = "用户登出")
    @GetMapping(value = "/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        //如果开启Redis集群注销所有keys缓存
        if(gemSystemProperties.isCluster()){
            String pattern = subject.getPrincipals() +"_"+ GemRedisKeys.Auth.PREFIX + "*";
            Set<String> keys = gemRedisUtils.keys(pattern);
            log.debug("注销所有keys="+keys);
            gemRedisUtils.delete(keys);
        }
        subject.logout();
        return "login";
    }
}