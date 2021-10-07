package com.markerhub.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("com.markerhub.mapper")
public class MybatisPlusConfig {
    /*
    新建一个包：通过@mapperScan注解指定要变成实现类的接口所在的包，
    然后包下面的所有接口在编译之后都会生成相应的实现类。PaginationInterceptor是一个分页插件。
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
