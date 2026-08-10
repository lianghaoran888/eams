package com.sky.aspect;

import com.sky.annotation.RateLimit;
import com.sky.constant.MessageConstant;
import com.sky.constant.RedisConstant;
import com.sky.context.BaseContext;
import com.sky.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 的接口限流切面（INCR + EXPIRE，固定窗口）
 */
@Aspect
@Component
@Slf4j
public class RateLimitAspect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Before("@annotation(rateLimit)")
    public void rateLimit(RateLimit rateLimit) {
        Long currentId = BaseContext.getCurrentId();
        String suffix = currentId == null ? "anonymous" : String.valueOf(currentId);
        String key = RedisConstant.RATE_LIMIT_KEY + rateLimit.key() + ":" + suffix;

        Long count = stringRedisTemplate.opsForValue().increment(key);
        if (count != null && count == 1L) {
            stringRedisTemplate.expire(key, rateLimit.period(), TimeUnit.SECONDS);
        }
        if (count != null && count > rateLimit.limit()) {
            log.warn("接口限流触发, key={}, count={}", key, count);
            throw new BaseException(MessageConstant.RATE_LIMIT_EXCEEDED);
        }
    }
}