package com.sky.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流注解（基于 Redis，默认每分钟最多 10 次）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /** 限流维度名称（用于拼装 Redis key） */
    String key() default "default";

    /** 时间窗口内允许的最大次数 */
    int limit() default 10;

    /** 时间窗口（秒） */
    int period() default 60;
}