package com.gemframework.generator.config;
import com.gemframework.common.exception.GemException;
import com.gemframework.generator.mapper.*;
import com.gemframework.mapper.GeneratorMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
public class DatabaseConfig {

    //默认mysql
    @Value("${gem.database: mysql}")
    private String database;

    @Autowired
    private MySQLGeneratorMapper mySQLGeneratorMapper;
    @Autowired
    private OracleGeneratorMapper oracleGeneratorMapper;
    @Autowired
    private SQLServerGeneratorMapper sqlServerGeneratorMapper;

    @Bean
    @Primary
    public GeneratorMapper get(){
        if("mysql".equalsIgnoreCase(database)){
            return mySQLGeneratorMapper;
        }else if("oracle".equalsIgnoreCase(database)){
            return oracleGeneratorMapper;
        }else if("sqlServer".equalsIgnoreCase(database)){
            return sqlServerGeneratorMapper;
        }else {
            throw new GemException("数据库配置有误：" + database);
        }
    }
}
