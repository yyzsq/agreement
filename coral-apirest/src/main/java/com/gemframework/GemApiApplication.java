
package com.gemframework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.gemframework.mapper")
public class GemApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(GemApiApplication.class, args);
    }

}
