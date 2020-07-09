
package com.gemframework.config.shiro;
import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.gemframework.config.shiro.session.GemCacheSessionDao;
import com.gemframework.config.shiro.session.GemSessionManager;
import com.gemframework.config.shiro.session.GemRedisSessionDao;
import com.gemframework.config.shiro.cache.GemCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Title: ShiroConfig
 * @Package: com.gemframework.config.shiro
 * @Date: 2020-03-27 13:16:03
 * @Version: v1.0
 * @Description: Shiro配置
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Configuration
public class ShiroConfig {

    /**
     * 配置shiro过滤器
     * @param securityManager
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //注册securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //设置登录URL
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // authc:所有url都必须认证通过才可以访问;
        // anon:所有url都都可以匿名访问;
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/oauth/**", "anon");
        filterChainDefinitionMap.put("/coral/**", "anon");
        filterChainDefinitionMap.put("/assets/**", "anon");
        filterChainDefinitionMap.put("/upload/**", "anon");
        filterChainDefinitionMap.put("/kaptcha/code", "anon");
        filterChainDefinitionMap.put("/captcha/code", "anon");
        filterChainDefinitionMap.put("/extend/phoneh5/**", "anon");
        filterChainDefinitionMap.put("/prekit/rbac/user/updsta", "anon");
        filterChainDefinitionMap.put("/extend/phoneh5/wechatNotice/**", "anon");
        //注意：这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
        filterChainDefinitionMap.put("/**", "authc");//剩余的都需要认证
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 注入安全管理器
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager(SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //注册自定义realm
        securityManager.setRealm(gemAuthRealm());
        //注册记住我
        securityManager.setRememberMeManager(cookieRememberMeManager());
        //配置自定义session管理，使用redis
        securityManager.setSessionManager(sessionManager);
        log.info("====SecurityManager注册完成====");
        return securityManager;
    }

    /**
     * 获取微信toncek
     * @return
     */
    public void gettonken(){

    }

    //注入自定义realm
    @Bean
    public GemAuthRealm gemAuthRealm() {
        GemAuthRealm gemAuthRealm = new GemAuthRealm();
        gemAuthRealm.setCachingEnabled(true);
        //开启缓存机制（也可以再SecurityManager中设置，在此开启只缓存realm信息）
        gemAuthRealm.setCacheManager(gemCacheManager());
        gemAuthRealm.setAuthorizationCachingEnabled(true);
        return gemAuthRealm;
    }

    //记住我管理器
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        SimpleCookie simpleCookie = new SimpleCookie(ShiroUtils.REMEMBERME_COOKIE_NAME);
        simpleCookie.setMaxAge(ShiroUtils.REMEMBERME_COOKIE_MAXAGE);
        cookieRememberMeManager.setCookie(simpleCookie);
        cookieRememberMeManager.setCipherKey(ShiroUtils.REMEMBERME_CIPHERKEY.getBytes());
        return cookieRememberMeManager;
    }


    /**
     * 集群环境开启REDIS-SESSION
     * 配置会话管理器
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "gem.system", name = "cluster", havingValue = "true")
    public SessionManager sessionManager() {
        GemSessionManager sessionManager = new GemSessionManager();
        sessionManager.setSessionDAO(cacheSessionDao());
        return sessionManager;
    }

    /**
     * 单机环境默认使用Shiro管理Session
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "gem.system", name = "cluster", havingValue = "false")
    public DefaultWebSessionManager defaultWebSessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        return sessionManager;
    }

    /**
     * 配置session持久化
     * @return
     */
    @Bean("redisSessionDao")
    public GemRedisSessionDao redisSessionDao(){
        GemRedisSessionDao gemRedisSessionDao = new GemRedisSessionDao();
        return gemRedisSessionDao;
    }

    /**
     * 配置开启缓存+Redis持久化
     * @return
     */
    @Bean("cacheSessionDao")
    public GemCacheSessionDao cacheSessionDao(){
        GemCacheSessionDao gemCacheSessionDao = new GemCacheSessionDao();
        return gemCacheSessionDao;
    }


    /**
     * 自定义Redis CacheManager
     * @return
     */
    @Bean("gemCacheManager")
    public GemCacheManager gemCacheManager(){
        GemCacheManager gemCacheManager = new GemCacheManager();
        return gemCacheManager;
    }


    //Shiro方言 用于页面标签/表达式
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }


    //开启注解
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public FilterRegistrationBean delegatingFilterProxy(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }
}