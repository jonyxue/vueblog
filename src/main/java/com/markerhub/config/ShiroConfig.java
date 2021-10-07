package com.markerhub.config;

import com.markerhub.shiro.AccountRealm;
import com.markerhub.shiro.JwtFilter;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    @Bean
    public SessionsSecurityManager securityManager(AccountRealm accountRealm,
                                                    SessionManager sessionManager,
                                                   RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);
//        //关联AccountRealm
//        securityManager.setRealm(accountRealm);
        //inject sessionManager
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(redisCacheManager);

        return securityManager;
    }
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/**", "jwt"); // 主要通过注解方式校验权限//authc默认的
        chainDefinition.addPathDefinitions(filterMap);
        return chainDefinition;
    }
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         ShiroFilterChainDefinition shiroFilterChainDefinition) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilter.setSecurityManager(securityManager);
        //shiro的内置过滤器
        /*
        anon:无需认证就可以访问
        authc:必须认证了才能访问
        user:必须拥有 记住我 功能才能用
        perms:拥有对某个资源的权限才能访问。
        role:拥有某个角色才能用
         */
        //拦截
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", jwtFilter);
        shiroFilter.setFilters(filters);
        Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();
        shiroFilter.setFilterChainDefinitionMap(filterMap);
//        或者
//        Map<String, String> filterMap = new LinkedHashMap<>();
//        授权,正常的情况下，没有授权会跳转到未授权页面
//        filterMap.put("/user/add","perms[user:add]");
//        filterMap.put("/user/update","perms[user:update]");
//        filterMap.put("/user/*","authc");
//        shiroFilter.setFilterChainDefinitionMap(filterMap);

//        //设置登录的请求
//        shiroFilter.setLoginUrl("/tologin");
        //未授权页面
//        shiroFilter.setUnauthorizedUrl("/xx");

        return shiroFilter;
    }


}







