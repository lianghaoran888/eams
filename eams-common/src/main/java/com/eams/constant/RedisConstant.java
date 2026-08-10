package com.eams.constant;

/**
 * Redis 缓存 Key 及过期时间常量
 */
public class RedisConstant {

    /** 登录 Token 缓存 key 前缀：login:token:{empId} -> token，7 天过期 */
    public static final String LOGIN_TOKEN_KEY = "login:token:";
    public static final long LOGIN_TOKEN_TTL = 7L * 24 * 60 * 60;

    /** 资产分类缓存 key：cache:assetCategory -> 分类列表 JSON，1 小时过期 */
    public static final String CATEGORY_CACHE_KEY = "cache:assetCategory:list";
    public static final long CATEGORY_CACHE_TTL = 60 * 60;

    /** 接口限流 key 前缀：rate:limit:{name}:{userId} */
    public static final String RATE_LIMIT_KEY = "rate:limit:";
}