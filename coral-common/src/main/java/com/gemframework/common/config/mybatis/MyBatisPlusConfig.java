
package com.gemframework.common.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Title: MyBatisPlusConfig
 * @Package: com.gemframework.config
 * @Date: 2020-03-09 14:42:33
 * @Version: v1.0
 * @Description: MyBatisPlusConfig配置
 * @Author: nine QQ 769990999
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */
@Slf4j
@Configuration
public class MyBatisPlusConfig {

    /**
     * @description: 配置分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        log.debug("注册分页插件");
        return new PaginationInterceptor();
    }
}