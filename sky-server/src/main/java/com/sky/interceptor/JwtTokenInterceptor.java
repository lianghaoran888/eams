package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.RedisConstant;
import com.sky.constant.RoleConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 统一 JWT 令牌校验拦截器：
 * 1. 校验 JWT 签名
 * 2. 校验 Redis 中登录态（Token 存 Redis，7 天过期）
 * 3. 管理端接口额外校验管理员角色
 */
@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行非 Controller 方法（静态资源等）
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(jwtProperties.getTokenName());
        if (token == null || token.isEmpty()) {
            response.setStatus(401);
            return false;
        }

        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());

            // 校验 Redis 登录态
            String redisToken = stringRedisTemplate.opsForValue().get(RedisConstant.LOGIN_TOKEN_KEY + empId);
            if (redisToken == null || !redisToken.equals(token)) {
                log.warn("Redis 登录态不存在或已失效, empId={}", empId);
                response.setStatus(401);
                return false;
            }

            // 管理端接口角色校验
            String uri = request.getRequestURI();
            if (uri.startsWith("/admin/")) {
                Object roleObj = claims.get(JwtClaimsConstant.ROLE);
                Integer role = roleObj == null ? null : Integer.valueOf(roleObj.toString());
                if (role == null || role != RoleConstant.ADMIN) {
                    log.warn("非管理员访问管理端接口, empId={}, uri={}", empId, uri);
                    response.setStatus(403);
                    return false;
                }
            }

            BaseContext.setCurrentId(empId);
            return true;
        } catch (Exception ex) {
            log.warn("JWT 校验失败: {}", ex.getMessage());
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        BaseContext.removeCurrentId();
    }
}