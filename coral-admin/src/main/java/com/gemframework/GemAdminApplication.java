
package com.gemframework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = {"com.gemframework.mapper","com.gemframework.generator.mapper"})
public class GemAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(GemAdminApplication.class, args);
    }

}
