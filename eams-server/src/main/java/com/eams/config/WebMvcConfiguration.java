package com.eams.config;

import com.eams.interceptor.JwtTokenInterceptor;
import com.eams.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * 配置类，注册 web 层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor;

    /**
     * 注册统一 JWT 拦截器：除登录接口外所有接口校验 JWT，管理端接口额外校验角色
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("注册统一 JWT 拦截器...");
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/admin/**")
                .addPathPatterns("/employee/**")
                .excludePathPatterns("/admin/employee/login")
                .excludePathPatterns("/employee/login");
    }

    /**
     * 扩展 Spring MVC 框架的消息转换器
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0, converter);
    }

    /**
     * 全局 Jackson 序列化器（用于 Redis 缓存 JSON 序列化等）
     */
    @Bean
    public JacksonObjectMapper jacksonObjectMapper() {
        return new JacksonObjectMapper();
    }
}