package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 统一配置（员工端与管理端共用同一套令牌）
 */
@Component
@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {

    /**
     * 签名密钥
     */
    private String secretKey;

    /**
     * 令牌有效期（毫秒），默认 7 天，与 Redis 中 token 缓存过期时间一致
     */
    private long ttl = 7L * 24 * 60 * 60 * 1000;

    /**
     * 前端传递令牌的请求头名称
     */
    private String tokenName;
}